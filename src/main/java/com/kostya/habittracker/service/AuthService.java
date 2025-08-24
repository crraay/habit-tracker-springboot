package com.kostya.habittracker.service;

import com.kostya.habittracker.dto.LoginRequest;
import com.kostya.habittracker.dto.LoginResponse;

public interface AuthService {
    
    LoginResponse login(LoginRequest loginRequest);
}
