package com.kostya.habittracker.entity;

import org.hibernate.annotations.ColumnDefault;
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
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(callSuper = true)
public class Habit extends BasicAudit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;
	
	@Column(nullable = false)
	String name;

	// TODO rework into streak entity
	@Column(nullable = false)
	@ColumnDefault("0")
	Integer streak = 0;
}
