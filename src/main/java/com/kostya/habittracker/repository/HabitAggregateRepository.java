package com.kostya.habittracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.HabitAggregate;

@org.springframework.stereotype.Repository
public interface HabitAggregateRepository extends Repository<HabitAggregate, Integer> {

	HabitAggregate save(HabitAggregate aggregate);

	Optional<HabitAggregate> findById(Integer habitId);

	@Query("SELECT ha FROM HabitAggregate ha WHERE ha.currentStreak > 0 AND ha.lastCheckInDate != :today")
	List<HabitAggregate> findAggregatesNeedingStreakReset(LocalDate today);
}
