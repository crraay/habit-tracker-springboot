package com.kostya.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HabitResponse {

	@NotBlank
	Integer id;
	
	@NotBlank
	String name;

	HabitAggregateResponse aggregate;

	Integer iconId;

	String iconUrl;
}
