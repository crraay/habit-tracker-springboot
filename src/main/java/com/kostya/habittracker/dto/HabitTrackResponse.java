package com.kostya.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitTrackResponse {
    
    Integer habitId;

    String habitName;

    HabitAggregateResponse aggregate;

    String iconUrl;

    Boolean status;
}
