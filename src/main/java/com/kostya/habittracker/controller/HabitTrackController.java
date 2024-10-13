package com.kostya.habittracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDate;

import com.kostya.habittracker.model.HabitTrackRequest;
import com.kostya.habittracker.model.HabitTrackResponse;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.HabitTrackService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Tag(name = "habit-track")
@RequestMapping("/api/habit-track")
public class HabitTrackController {
    
    @Autowired
    HabitTrackService habitTrackService;

    @GetMapping("/{date}")
    List<HabitTrackResponse> getTrackingList(
        @PathVariable LocalDate date,
        @AuthenticationPrincipal UserDetails userDetails) {

        return this.habitTrackService.getTrackingList(date, userDetails.getUser());
    }

    @PostMapping("")
    public void trackHabit(
        @RequestBody HabitTrackRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        this.habitTrackService.trackHabit(request, userDetails.getUser());
    }

    @DeleteMapping("")
    public void untrackHabit(
        @RequestBody HabitTrackRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        this.habitTrackService.untrackHabit(request, userDetails.getUser());
    }
}
