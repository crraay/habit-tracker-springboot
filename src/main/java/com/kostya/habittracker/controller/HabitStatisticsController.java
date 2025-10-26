package com.kostya.habittracker.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.dto.HabitStatResponse;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.HabitStatisticsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Tag(name = "habit-statistics")
@RequestMapping("/api/statistics")
@LogExecution
public class HabitStatisticsController {

    @Autowired
    private HabitStatisticsService habitStatisticsService;

    @GetMapping("/{startDate}/{endDate}")
    ResponseEntity<List<HabitStatResponse>> getAggregatedData(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<HabitStatResponse> body = this.habitStatisticsService.getAggregatedData(startDate, endDate, userDetails.getUser());
        return ResponseEntity.ok(body);
    }
}
