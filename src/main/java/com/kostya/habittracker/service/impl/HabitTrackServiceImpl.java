package com.kostya.habittracker.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.dto.HabitTrackRequest;
import com.kostya.habittracker.dto.HabitTrackResponse;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.ConflictException;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;
import com.kostya.habittracker.service.HabitTrackService;
import com.kostya.habittracker.service.StreakService;

@Service
public class HabitTrackServiceImpl implements HabitTrackService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private StreakService streakService;

    @Override
    public List<HabitTrackResponse> getTrackingList(LocalDate date, User user) {
        List<Habit> habits = this.habitRepository.findAllByUserId(user.getId());
        List<HabitLog> habitLogs = this.habitLogRepository.findByDate(date);

        return habits.stream().map(habit -> {
            boolean status = habitLogs.stream()
                .anyMatch(log -> log.getHabit().getId().equals(habit.getId()));

            return new HabitTrackResponse(habit.getId(), habit.getName(), habit.getStreak(), status);
        }).collect(Collectors.toList());
    }

    @Override
    public void trackHabit(HabitTrackRequest trackRequest, User user) {
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        if (this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate()) != null) {
            throw new ConflictException("Habit log already exists for the given habit and date");
        }
        
        // create habit log (track)
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setDate(trackRequest.getDate());
        this.habitLogRepository.save(habitLog);

        // update streak
        Integer streak = this.streakService.calculateStreak(habit);
        habit.setStreak(streak);
        this.habitRepository.save(habit);
    }

    @Override
    public void untrackHabit(HabitTrackRequest trackRequest, User user) {
        // find habit log by habit and date
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        HabitLog habitLog = this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate());
        if (habitLog == null) {
            throw new ConflictException("Habit log not found for the given habit and date");
        }

        // delete habit log (untrack)
        this.habitLogRepository.delete(habitLog);

        // update streak
        Integer streak = this.streakService.calculateStreak(habit);
        habit.setStreak(streak);
        this.habitRepository.save(habit);
    }
}
