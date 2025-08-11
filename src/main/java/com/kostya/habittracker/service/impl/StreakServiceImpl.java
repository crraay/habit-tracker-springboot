package com.kostya.habittracker.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.service.StreakService;

@Service
public class StreakServiceImpl implements StreakService {
    
    @Autowired
    private HabitLogRepository habitLogRepository;

    public Integer calculateStreak(Habit habit) {
        Integer streak = 0;
        List<HabitLog> habitLogs = habitLogRepository.findByHabitOrderByDateDesc(habit);
        LocalDate lastCheckedDate = null;

        for (HabitLog log : habitLogs) {
            if (lastCheckedDate == null || log.getDate().equals(lastCheckedDate.minusDays(1))) {
                streak++;
                lastCheckedDate = log.getDate();
            } else {
                break;
            }
        }

        return streak;
    }
}
