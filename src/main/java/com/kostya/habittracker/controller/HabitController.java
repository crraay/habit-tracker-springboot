package com.kostya.habittracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;
import com.kostya.habittracker.service.HabitService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "habit-mgmt")
@RequestMapping("/api/habit")
public class HabitController {
	
	@Autowired
	HabitService habitService;
	
	@GetMapping("")
	List<HabitResponse> getHabits() {
		return this.habitService.getHabits();
	}
	
	@GetMapping("/{id}")
	HabitResponse getHabit(@PathVariable("id") Integer id) {
		return this.habitService.getHabit(id);
	}
	
	@PostMapping("")
	HabitResponse createHabit(@RequestBody HabitRequest request) {
		return this.habitService.createHabit(request);
	}
	
	@PutMapping("/{id}")
	HabitResponse updateHabit(@PathVariable("id") Integer id, @RequestBody HabitRequest request) {
		return this.habitService.updateHabit(id, request);
	}
	
	@DeleteMapping("/{id}")
	void deleteHabit(@PathVariable("id") Integer id) {
		this.habitService.deleteHabit(id);
	}
}
