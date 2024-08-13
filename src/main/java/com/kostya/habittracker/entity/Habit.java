package com.kostya.habittracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Habit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	String name;
	
}
