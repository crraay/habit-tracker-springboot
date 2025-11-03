package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Builder
public class HabitDTO {
	
	// Response fields (nullable when used as request)
	private final Integer id;
	
	@NotBlank
	@Size(max = 100)
	private final String name;

	// Response-only fields (null when used as request)
	private final HabitAggregateResponse aggregate;

	private final Integer iconId;

	private final String iconUrl;
}

