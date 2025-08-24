package com.kostya.habittracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.time.LocalDate;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.dto.HabitTrackRequest;
import com.kostya.habittracker.dto.HabitTrackResponse;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.HabitTrackService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;


@RestController
@Tag(name = "habit-track")
@RequestMapping("/api/habit-track")
@LogExecution
public class HabitTrackController {
    
    @Autowired
    HabitTrackService habitTrackService;

    @GetMapping("/{date}")
    ResponseEntity<List<HabitTrackResponse>> getTrackingList(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserDetails userDetails) {

        List<HabitTrackResponse> body = this.habitTrackService.getTrackingList(date, userDetails.getUser());
        return ResponseEntity.ok(body);
    }

    @PostMapping("")
    public ResponseEntity<Void> trackHabit(
        @Valid @RequestBody HabitTrackRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        this.habitTrackService.trackHabit(request, userDetails.getUser());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> untrackHabit(
        @Valid @RequestBody HabitTrackRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        this.habitTrackService.untrackHabit(request, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
