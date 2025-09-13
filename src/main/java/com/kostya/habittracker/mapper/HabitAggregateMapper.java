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
		HabitAggregateResponse response = new HabitAggregateResponse();
		response.setTotalCheckIns(aggregate.getTotalCheckIns());
		response.setCurrentStreak(aggregate.getCurrentStreak());
		response.setBestStreak(aggregate.getBestStreak());
		response.setStreakStartDate(aggregate.getStreakStartDate());
		response.setLastCheckInDate(aggregate.getLastCheckInDate());
		return response;
	}
}
