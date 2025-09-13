package com.kostya.habittracker.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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

	@OneToOne(mappedBy = "habit", fetch = FetchType.LAZY, optional = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	HabitAggregate aggregate;
}
