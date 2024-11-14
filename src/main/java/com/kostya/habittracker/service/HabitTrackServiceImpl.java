package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.ConflictException;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.model.HabitTrackRequest;
import com.kostya.habittracker.model.HabitTrackResponse;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;

@Service
public class HabitTrackServiceImpl implements HabitTrackService {

    private static final Logger logger = LoggerFactory.getLogger(HabitTrackServiceImpl.class);

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Override
    public List<HabitTrackResponse> getTrackingList(LocalDate date, User user) {
        List<Habit> habits = this.habitRepository.findAllByUserId(user.getId());
        List<HabitLog> habitLogs = this.habitLogRepository.findByDate(date);

        return habits.stream().map(habit -> {
            boolean status = habitLogs.stream()
                .anyMatch(log -> log.getHabit().getId().equals(habit.getId()));

            return new HabitTrackResponse(habit.getId(), habit.getName(), status);
        }).collect(Collectors.toList());
    }

    @Override
    public void trackHabit(HabitTrackRequest trackRequest, User user) {
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        if (this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate()) != null) {
            throw new ConflictException("Habit log already exists for the given habit and date");
        }
        
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setDate(trackRequest.getDate());

        this.habitLogRepository.save(habitLog);
    }

    @Override
    public void untrackHabit(HabitTrackRequest trackRequest, User user) {
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        HabitLog habitLog = this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate());
        if (habitLog == null) {
            throw new ConflictException("Habit log not found for the given habit and date");
        }

        this.habitLogRepository.delete(habitLog);
    }
}
