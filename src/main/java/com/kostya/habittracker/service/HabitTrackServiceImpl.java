package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.model.HabitTrackRequest;
import com.kostya.habittracker.model.HabitTrackResponse;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;

@Service
public class HabitTrackServiceImpl implements HabitTrackService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Override
    public List<HabitTrackResponse> getTrackingList(LocalDate date) {
        List<Habit> habits = this.habitRepository.findAll();
        List<HabitLog> habitLogs = this.habitLogRepository.findByDate(date);

        return habits.stream().map(habit -> {
            boolean status = habitLogs.stream()
                                      .anyMatch(log -> log.getHabit().getId().equals(habit.getId()));
            return new HabitTrackResponse(habit.getId(), habit.getName(), status);
        }).collect(Collectors.toList());
    }

    @Override
    public void trackHabit(HabitTrackRequest trackRequest) {
        Habit habit = this.habitRepository.findById(trackRequest.getHabitId())
                                         .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate()) != null) {
            throw new RuntimeException("Habit log already exists for the given habit and date");
        }
        
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setDate(trackRequest.getDate());
        this.habitLogRepository.save(habitLog);
    }

    @Override
    public void untrackHabit(HabitTrackRequest trackRequest) {
        Habit habit = this.habitRepository.findById(trackRequest.getHabitId())
                                         .orElseThrow(() -> new RuntimeException("Habit not found"));

        HabitLog habitLog = this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate());
        if (habitLog == null) {
            throw new RuntimeException("Habit log not found for the given habit and date");
        }

        this.habitLogRepository.delete(habitLog);
    }
}
