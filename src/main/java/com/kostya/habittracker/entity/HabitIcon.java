package com.kostya.habittracker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "habit_icon", indexes = {
	@Index(name = "idx_habit_icon_active", columnList = "is_active")
})
@EqualsAndHashCode(callSuper = true)
public class HabitIcon extends BasicAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(nullable = false)
	String name;

	@Column
	String description;

	@Column(name="s3_url", nullable = false, length = 500)
	String s3Url;

	@Column(nullable = false)
	Boolean isActive = true;
}
