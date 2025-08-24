package com.kostya.habittracker.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class ErrorResponse {

    Integer status;

    String error;

    String message;

    String path;

    String correlationId;

    Instant timestamp;
}
