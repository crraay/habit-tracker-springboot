package com.kostya.habittracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;

@org.springframework.stereotype.Repository
public interface HabitLogRepository extends Repository<HabitLog, Integer> {

	HabitLog save(HabitLog habitLog);

	void delete(HabitLog habitLog);

	List<HabitLog> findByDate(LocalDate date);

	HabitLog findByHabitAndDate(Habit habit, LocalDate date);

    List<HabitLog> findByHabitOrderByDateDesc(Habit habit);

	// TODO check execution plan
	@Query("SELECT hl FROM HabitLog hl WHERE hl.habit.user = :user AND hl.date BETWEEN :startDate AND :endDate")
    List<HabitLog> findByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
