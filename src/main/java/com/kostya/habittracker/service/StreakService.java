package com.kostya.habittracker.service;

import com.kostya.habittracker.entity.Habit;

public interface StreakService {
    
    public Integer calculateStreak(Habit habit);
}
