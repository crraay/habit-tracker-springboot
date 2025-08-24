package com.kostya.habittracker.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.dto.HabitStat;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;
import com.kostya.habittracker.service.HabitStatisticsService;

@Service
public class HabitStatisticsServiceImpl implements HabitStatisticsService {

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Override
    public List<HabitStat> getAggregatedData(LocalDate startDate, LocalDate endDate, User currentUser) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        // Get all habits for the user
        List<Habit> userHabits = habitRepository.findAllByUserId(currentUser.getId());
        
        // Get habit logs for the date range
        List<HabitLog> habitLogs = habitLogRepository.findByUserAndDateBetween(currentUser, startDate, endDate);
        
        // Create a map of habit ID to completion count for efficient lookup
        Map<Integer, Long> habitCompletionCounts = habitLogs.stream()
            .collect(Collectors.groupingBy(
                log -> log.getHabit().getId(),
                Collectors.counting()
            ));

        // Map all user habits to statistics, including those with zero completions
        return userHabits.stream()
            .map(habit -> {
                HabitStat stats = new HabitStat();
                stats.setHabitId(habit.getId());
                stats.setHabitName(habit.getName());
                stats.setDone(habitCompletionCounts.getOrDefault(habit.getId(), 0L).intValue());
                stats.setOf((int) totalDays);
                return stats;
            })
            .collect(Collectors.toList());
    }
}
