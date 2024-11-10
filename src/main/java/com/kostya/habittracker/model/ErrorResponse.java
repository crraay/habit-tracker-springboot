package com.kostya.habittracker.model;

import lombok.Data;

@Data
public class ErrorResponse {

    Integer status;

    String error;

    String message;
}
