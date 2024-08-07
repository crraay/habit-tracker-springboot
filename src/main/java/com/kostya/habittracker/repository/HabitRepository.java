package com.kostya.habittracker.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kostya.habittracker.entity.Habit;

@Repository
public interface HabitRepository extends CrudRepository<Habit, Integer> {

	List<Habit> findAll();
}
