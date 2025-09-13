package com.kostya.habittracker.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitAggregateResponse {
    
    private Integer totalCheckIns;

    private Integer currentStreak;

    private Integer bestStreak;

    private LocalDate streakStartDate;

    private LocalDate lastCheckInDate;
}
