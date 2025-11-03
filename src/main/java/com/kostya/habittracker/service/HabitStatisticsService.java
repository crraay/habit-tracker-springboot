package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;

import com.kostya.habittracker.dto.HabitStatResponse;
import com.kostya.habittracker.entity.User;

public interface HabitStatisticsService {
    
    List<HabitStatResponse> getAggregatedData(LocalDate startDate, LocalDate endDate, User currentUser);
}
