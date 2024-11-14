package com.kostya.habittracker.model;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequest {

    String username;

    @ToString.Exclude
    String password;

    @ToString.Include(name = "password")
    private String passwordMasked() { return password == null ? null : "*****"; }
}
