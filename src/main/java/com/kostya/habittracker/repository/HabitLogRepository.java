package com.kostya.habittracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Integer> {

	List<HabitLog> findByDate(LocalDate date);

	HabitLog findByHabitAndDate(Habit habit, LocalDate date);
}
