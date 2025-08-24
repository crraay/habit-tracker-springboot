package com.kostya.habittracker.dto;

import java.time.LocalDate;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Data
public class HabitTrackRequest {
    
    @NotNull
    Integer habitId;

    @NotNull
    @PastOrPresent
    LocalDate date;
}
