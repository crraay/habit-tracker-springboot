package com.kostya.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitTrackResponse {
    
    Integer habitId;

    String habitName;

    Integer streak;

    Boolean status;
}
