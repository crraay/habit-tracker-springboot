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
		HabitTrackResponse response = new HabitTrackResponse();
		response.setHabitId(habit.getId());
		response.setHabitName(habit.getName());
		response.setAggregate(habitAggregateMapper.toResponse(habit.getAggregate()));
		response.setIconUrl(habit.getIcon() != null ? habit.getIcon().getS3Url() : null);
		response.setStatus(status);
		return response;
	}
}
