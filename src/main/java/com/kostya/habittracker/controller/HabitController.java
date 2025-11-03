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
import com.kostya.habittracker.dto.HabitDTO;
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
	ResponseEntity<List<HabitDTO>> getHabits(
		@AuthenticationPrincipal UserDetails userDetails) {

		List<HabitDTO> body = this.habitService.getHabits(userDetails.getUser());
		return ResponseEntity.ok(body);
	}
	
	@GetMapping("/{id}")
	ResponseEntity<HabitDTO> getHabit(
		@PathVariable Integer id,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitDTO body = this.habitService.getHabit(id, userDetails.getUser());
		return ResponseEntity.ok(body);
	}
	
	@PostMapping("")
	ResponseEntity<HabitDTO> createHabit(
		@Valid @RequestBody HabitDTO dto,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitDTO body = this.habitService.createHabit(dto, userDetails.getUser());
		return ResponseEntity.status(201).body(body);
	}
	
	@PutMapping("/{id}")
	ResponseEntity<HabitDTO> updateHabit(
		@PathVariable Integer id,
		@Valid @RequestBody HabitDTO dto,
		@AuthenticationPrincipal UserDetails userDetails) {

		HabitDTO body = this.habitService.updateHabit(id, dto, userDetails.getUser());
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
