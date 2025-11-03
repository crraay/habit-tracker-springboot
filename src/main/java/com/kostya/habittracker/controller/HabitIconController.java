package com.kostya.habittracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.dto.HabitIconDTO;
import com.kostya.habittracker.service.HabitIconService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Tag(name = "habit-icons")
@RequestMapping("/api/habit-icons")
@LogExecution
public class HabitIconController {
    
    @Autowired
    HabitIconService habitIconService;

    @GetMapping
    public ResponseEntity<List<HabitIconDTO>> getAll() {
        return ResponseEntity.ok(habitIconService.getAll());
    }
    
}
