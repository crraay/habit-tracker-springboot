package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorResponse {

    private final Integer status;

    private final String error;

    private final String message;

    private final String path;

    private final String correlationId;

    private final Instant timestamp;
}
