package com.kostya.habittracker.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HabitTrackRequest {
    
    Integer habitId;

    LocalDate date;
}
