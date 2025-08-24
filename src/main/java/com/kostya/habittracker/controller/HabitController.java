package com.kostya.habittracker.controller;

import java.util.List;

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
import org.springframework.http.ResponseEntity;

import com.kostya.habittracker.model.UserDetails;
import com.kostya.habittracker.annotation.LogExecution;
import com.kostya.habittracker.dto.HabitRequest;
import com.kostya.habittracker.dto.HabitResponse;
import com.kostya.habittracker.service.HabitService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "habit-mgmt")
@RequestMapping("/api/habit")
@LogExecution
public class HabitController {
	
	@Autowired
	HabitService habitService;

	@GetMapping("")
	ResponseEntity<List<HabitResponse>> getHabits(
		@AuthenticationPrincipal UserDetails userDetails) {

		List<HabitResponse> body = this.habitService.getHabits(userDetails.getUser());
		return ResponseEntity.ok(body);
	}
	
	@GetMapping("/{id}")
	ResponseEntity<HabitResponse> getHabit(
		@PathVariable Integer id,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitResponse body = this.habitService.getHabit(id, userDetails.getUser());
		return ResponseEntity.ok(body);
	}
	
	@PostMapping("")
	ResponseEntity<HabitResponse> createHabit(
		@Valid @RequestBody HabitRequest request,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitResponse body = this.habitService.createHabit(request, userDetails.getUser());
		return ResponseEntity.status(201).body(body);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<HabitResponse> updateHabit(
		@PathVariable Integer id,
		@Valid @RequestBody HabitRequest request,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitResponse body = this.habitService.updateHabit(id, request, userDetails.getUser());
		return ResponseEntity.ok(body);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteHabit(
		@PathVariable Integer id,
		@AuthenticationPrincipal UserDetails userDetails) {

		this.habitService.deleteHabit(id, userDetails.getUser());
		return ResponseEntity.noContent().build();
	}
}
