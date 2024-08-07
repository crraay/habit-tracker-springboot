package com.kostya.habittracker.model;

import com.kostya.habittracker.entity.Habit;

import lombok.Data;

@Data
public class HabitRequest {
	
	String name;
	
	public Habit convert() {
		Habit entity = new Habit();
		
		entity.setName(this.name);
		
		return entity;
	}
	
	public Habit convert(Integer id) {
		Habit entity = this.convert();
		
		entity.setId(id);
		
		return entity;
	}
}
