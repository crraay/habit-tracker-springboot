package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Builder
public class HabitRequest {
	
	@NotBlank
	@Size(max = 100)
	private final String name;

	private final Integer iconId;
}
