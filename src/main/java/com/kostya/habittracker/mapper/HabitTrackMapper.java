package com.kostya.habittracker.mapper;

import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitTrackResponse;
import com.kostya.habittracker.entity.Habit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HabitTrackMapper {

	private final HabitAggregateMapper habitAggregateMapper;

	public HabitTrackResponse toResponse(Habit habit, Boolean status) {
		if (habit == null) {
			return null;
		}
		return HabitTrackResponse.builder()
			.habitId(habit.getId())
			.habitName(habit.getName())
			.aggregate(habitAggregateMapper.toResponse(habit.getAggregate()))
			.iconUrl(habit.getIcon() != null ? habit.getIcon().getS3Url() : null)
			.status(status)
			.build();
	}
}
