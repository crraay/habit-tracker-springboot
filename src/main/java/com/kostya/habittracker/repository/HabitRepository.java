package com.kostya.habittracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.Habit;

@org.springframework.stereotype.Repository
public interface HabitRepository extends Repository<Habit, Integer> {

	Habit save(Habit habit);

	void delete(Habit habit);

	List<Habit> findAllByUserId(Integer userId);

	Optional<Habit> findByIdAndUserId(Integer id, Integer userId);
}
