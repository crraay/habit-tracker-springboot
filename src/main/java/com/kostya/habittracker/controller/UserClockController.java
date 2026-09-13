package com.kostya.habittracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.dto.UpdateTimezoneRequest;
import com.kostya.habittracker.dto.UserClockResponse;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.UserClockService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "user-clock")
@RequestMapping("/api/me")
@LogExecution
public class UserClockController {

    @Autowired
    private UserClockService userClockService;

    @GetMapping
    public ResponseEntity<UserClockResponse> getMe(
        @AuthenticationPrincipal UserDetails userDetails) {

        UserClockResponse body = this.userClockService.getCurrentUserClock(userDetails.getUser());
        return ResponseEntity.ok(body);
    }

    @PatchMapping
    public ResponseEntity<UserClockResponse> updateMe(
        @Valid @RequestBody UpdateTimezoneRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        UserClockResponse body = this.userClockService.updateTimezone(userDetails.getUser(), request.getTimezone());
        return ResponseEntity.ok(body);
    }
}
