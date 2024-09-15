package com.kostya.habittracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    
    String token;

    Integer tokenExpiresIn;
}
