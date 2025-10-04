package com.kostya.habittracker.service;

import java.time.LocalDate;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitAggregate;

public interface HabitAggregateService {

	HabitAggregate getOrCreateAggregate(Habit habit);

	void applyTrack(Habit habit, LocalDate date);

	void applyUntrack(Habit habit, LocalDate date);

	void recomputeAggregate(Habit habit);

	void resetStreaksForUncheckedHabits(LocalDate today);
}


