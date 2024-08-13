package com.kostya.habittracker.model;

import com.kostya.habittracker.entity.Habit;

import lombok.Data;

@Data
public class HabitResponse {

	Integer id;
	
	String name;
	
	static public HabitResponse convert(Habit entity) {
		HabitResponse response = new HabitResponse();
		
		response.id = entity.getId();
		response.name = entity.getName();
		
		return response;
	}
}
