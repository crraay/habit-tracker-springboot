package com.kostya.habittracker.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.HabitAggregate;

@org.springframework.stereotype.Repository
public interface HabitAggregateRepository extends Repository<HabitAggregate, Integer> {

	HabitAggregate save(HabitAggregate aggregate);

	Optional<HabitAggregate> findById(Integer habitId);
}


