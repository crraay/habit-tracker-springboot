package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;

import com.kostya.habittracker.dto.HabitStat;
import com.kostya.habittracker.entity.User;

public interface HabitStatisticsService {
    
    List<HabitStat> getAggregatedData(LocalDate startDate, LocalDate endDate, User currentUser);
}
