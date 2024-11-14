package com.kostya.habittracker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.model.LoginRequest;
import com.kostya.habittracker.model.LoginResponse;
import com.kostya.habittracker.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "auth")
@RequestMapping("/api/auth")
@LogExecution
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
}
