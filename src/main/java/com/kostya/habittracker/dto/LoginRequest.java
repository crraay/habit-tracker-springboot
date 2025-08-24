package com.kostya.habittracker.dto;

import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotBlank
    @Size(max = 100)
    String username;

    @ToString.Exclude
    @NotBlank
    // TODO add min length
    @Size(max = 100)
    String password;

    @ToString.Include(name = "password")
    private String passwordMasked() { return password == null ? null : "*****"; }
}
