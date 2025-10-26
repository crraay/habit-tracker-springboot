package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Builder
@ToString
public class LoginRequest {

    @NotBlank
    @Size(max = 100)
    private final String username;

    @ToString.Exclude
    @NotBlank
    // TODO add min length
    @Size(max = 100)
    private final String password;

    @ToString.Include(name = "password")
    private String passwordMasked() { return password == null ? null : "*****"; }
}
