package com.kostya.habittracker.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Habit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;
	
	String name;
}
