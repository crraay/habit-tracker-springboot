package com.kostya.habittracker.dto;

import com.kostya.habittracker.entity.Habit;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class HabitRequest {
	
	@NotBlank
	@Size(max = 100)
	String name;
	
	// TODO remove
	public Habit convert() {
		Habit entity = new Habit();
		
		entity.setName(this.name);
		
		return entity;
	}
	
	// remove
	public Habit convert(Integer id) {
		Habit entity = this.convert();
		
		entity.setId(id);
		
		return entity;
	}
}
