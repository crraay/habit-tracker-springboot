package com.kostya.habittracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;
import com.kostya.habittracker.service.HabitService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "habit-mgmt")
@RequestMapping("/api/habit")
public class HabitController {

	private static final Logger logger = LoggerFactory.getLogger(HabitController.class);
	
	@Autowired
	HabitService habitService;

	@GetMapping("")
	List<HabitResponse> getHabits(
		@AuthenticationPrincipal UserDetails userDetails) {

		return this.habitService.getHabits(userDetails.getUser());
	}
	
	@GetMapping("/{id}")
	HabitResponse getHabit(
		@PathVariable Integer id,
		@AuthenticationPrincipal UserDetails userDetails) {

		return this.habitService.getHabit(id, userDetails.getUser());
	}
	
	@PostMapping("")
	HabitResponse createHabit(
		@RequestBody HabitRequest request,
		@AuthenticationPrincipal UserDetails userDetails) {

		return this.habitService.createHabit(request, userDetails.getUser());
	}
	
	@PutMapping("/{id}")
	HabitResponse updateHabit(
		@PathVariable Integer id,
		@RequestBody HabitRequest request,
		@AuthenticationPrincipal UserDetails userDetails) {

		return this.habitService.updateHabit(id, request, userDetails.getUser());
	}
	
	@DeleteMapping("/{id}")
	void deleteHabit(
		@PathVariable Integer id,
		@AuthenticationPrincipal UserDetails userDetails) {

		this.habitService.deleteHabit(id, userDetails.getUser());
	}
}
