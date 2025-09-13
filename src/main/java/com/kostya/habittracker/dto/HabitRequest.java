package com.kostya.habittracker.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class HabitRequest {
	
	@NotBlank
	@Size(max = 100)
	String name;
}
