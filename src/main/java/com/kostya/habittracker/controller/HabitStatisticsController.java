package com.kostya.habittracker.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.model.HabitStat;
import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.service.HabitStatisticsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Tag(name = "habit-statistics")
@RequestMapping("/api/statistics")
@LogExecution
public class HabitStatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(HabitStatisticsController.class);

    @Autowired
    private HabitStatisticsService habitStatisticsService;

    @GetMapping("/{startDate}/{endDate}")
    List<HabitStat> getAggregatedData(
        @PathVariable LocalDate startDate,
        @PathVariable LocalDate endDate,
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        return this.habitStatisticsService.getAggregatedData(startDate, endDate, userDetails.getUser());
    }
}
