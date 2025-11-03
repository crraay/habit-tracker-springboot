package com.kostya.habittracker.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Getter
@Builder
public class HabitTrackRequest {
    
    @NotNull
    private final Integer habitId;

    @NotNull
    @PastOrPresent
    private final LocalDate date;
}
