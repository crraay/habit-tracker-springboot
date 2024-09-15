package com.kostya.habittracker.service;

import com.kostya.habittracker.model.LoginRequest;
import com.kostya.habittracker.model.LoginResponse;

public interface AuthService {
    
    LoginResponse login(LoginRequest loginRequest);
}
