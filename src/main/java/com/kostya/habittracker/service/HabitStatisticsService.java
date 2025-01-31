package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;

import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.model.HabitStat;

public interface HabitStatisticsService {
    
    public List<HabitStat> getAggregatedData(LocalDate startDate, LocalDate endDate, User currentUser);
}
