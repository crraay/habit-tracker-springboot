package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitStatResponse {

    private final Integer habitId;

    private final String habitName;

    private final Integer done;

    private final Integer of;

    private final HabitAggregateResponse aggregate;

    private final String iconUrl;
}
