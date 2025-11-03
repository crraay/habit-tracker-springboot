package com.kostya.habittracker.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitAggregateResponse {
    
    private final Integer totalCheckIns;

    private final Integer currentStreak;

    private final Integer bestStreak;

    private final LocalDate streakStartDate;

    private final LocalDate lastCheckInDate;
}
