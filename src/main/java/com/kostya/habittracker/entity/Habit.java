package com.kostya.habittracker.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Habit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;
	
	@Column(nullable = false)
	String name;
}
