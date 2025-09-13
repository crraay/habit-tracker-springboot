package com.kostya.habittracker.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "habit_aggregate", indexes = {
	@Index(name = "idx_habit_aggregate_last_check_in_date", columnList = "last_check_in_date")
})
@EqualsAndHashCode(callSuper = true)
public class HabitAggregate extends BasicAudit {

	@Id
	Integer habitId;

	@OneToOne(optional = false)
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	Habit habit;

	@Column(nullable = false)
	@ColumnDefault("0")
	Integer totalCheckIns = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	Integer currentStreak = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	Integer bestStreak = 0;

	LocalDate lastCheckInDate;

	LocalDate streakStartDate;
}


