package com.kostya.habittracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDate;

import com.kostya.habittracker.model.HabitTrackRequest;
import com.kostya.habittracker.model.HabitTrackResponse;
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
    List<HabitTrackResponse> getTrackingList(@PathVariable("date") LocalDate date) {
        return this.habitTrackService.getTrackingList(date);
    }

    @PostMapping("")
    public void trackHabit(@RequestBody HabitTrackRequest request) {
        this.habitTrackService.trackHabit(request);
    }

    @DeleteMapping("")
    public void untrackHabit(@RequestBody HabitTrackRequest request) {
        this.habitTrackService.untrackHabit(request);
    }
}
