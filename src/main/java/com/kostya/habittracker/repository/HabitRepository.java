package com.kostya.habittracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.Habit;

@org.springframework.stereotype.Repository
public interface HabitRepository extends Repository<Habit, Integer> {

	Habit save(Habit habit);

	void delete(Habit habit);

	@Query("select h from Habit h left join fetch h.aggregate where h.user.id = :userId")
	List<Habit> findAllByUserId(@Param("userId") Integer userId);

	Optional<Habit> findByIdAndUserId(Integer id, Integer userId);

	@Query("select h from Habit h left join fetch h.aggregate where h.id = :id and h.user.id = :userId")
	Optional<Habit> findWithAggregateByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);
}
