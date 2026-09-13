package com.kostya.habittracker.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserClockResponse {

    private final String timezone;

    private final LocalDate today;
}
