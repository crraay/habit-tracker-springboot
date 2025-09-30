package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorResponse {

    Integer status;

    String error;

    String message;

    String path;

    String correlationId;

    Instant timestamp;
}
