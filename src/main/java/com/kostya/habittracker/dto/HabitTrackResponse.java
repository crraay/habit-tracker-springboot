package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitTrackResponse {
    
    private final Integer habitId;

    private final String habitName;

    private final HabitAggregateResponse aggregate;

    private final String iconUrl;

    private final Boolean status;
}
