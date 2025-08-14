package com.kostya.habittracker.service;

import com.kostya.habittracker.entity.Habit;

public interface StreakService {
    
    Integer calculateStreak(Habit habit);
}
