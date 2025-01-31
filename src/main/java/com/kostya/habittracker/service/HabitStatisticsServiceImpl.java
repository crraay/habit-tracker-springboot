package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.model.HabitStat;
import com.kostya.habittracker.repository.HabitLogRepository;

@Service
public class HabitStatisticsServiceImpl implements HabitStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(HabitStatisticsServiceImpl.class);

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Override
    public List<HabitStat> getAggregatedData(LocalDate startDate, LocalDate endDate, User currentUser) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        List<HabitLog> habitLogs = habitLogRepository.findByUserAndDateBetween(currentUser, startDate, endDate);

        return habitLogs.stream()
            .collect(Collectors.groupingBy(HabitLog::getHabit))
            .entrySet().stream()
            .map(entry -> {
                HabitStat stats = new HabitStat();
                stats.setHabitId(entry.getKey().getId());
                stats.setHabitName(entry.getKey().getName());
                stats.setDone(entry.getValue().size());
                stats.setOf((int) totalDays);
                return stats;
            })
            .collect(Collectors.toList());
    }
}
