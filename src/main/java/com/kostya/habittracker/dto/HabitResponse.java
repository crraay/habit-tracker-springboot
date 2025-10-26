package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitResponse {

	private final Integer id;
	
	private final String name;

	private final HabitAggregateResponse aggregate;

	private final Integer iconId;

	private final String iconUrl;
}
