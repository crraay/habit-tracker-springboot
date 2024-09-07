package com.kostya.habittracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitTrackResponse {
    
    Integer habitId;

    String habitName;

    Boolean status;
}
