package com.kostya.habittracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;

@org.springframework.stereotype.Repository
public interface HabitLogRepository extends Repository<HabitLog, Integer> {

	HabitLog save(HabitLog habitLog);

	void delete(HabitLog habitLog);

	List<HabitLog> findByDate(LocalDate date);

	HabitLog findByHabitAndDate(Habit habit, LocalDate date);
}
