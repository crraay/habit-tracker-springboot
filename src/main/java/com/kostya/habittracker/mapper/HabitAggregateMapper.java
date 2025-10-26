package com.kostya.habittracker.mapper;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitAggregateResponse;
import com.kostya.habittracker.entity.HabitAggregate;

@Component
public class HabitAggregateMapper {

	public HabitAggregateResponse toResponse(@Nullable HabitAggregate aggregate) {
		if (aggregate == null) {
			return null;
		}
		return HabitAggregateResponse.builder()
			.totalCheckIns(aggregate.getTotalCheckIns())
			.currentStreak(aggregate.getCurrentStreak())
			.bestStreak(aggregate.getBestStreak())
			.streakStartDate(aggregate.getStreakStartDate())
			.lastCheckInDate(aggregate.getLastCheckInDate())
			.build();
	}
}
