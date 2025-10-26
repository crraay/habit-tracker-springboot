package com.kostya.habittracker.mapper;

import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitRequest;
import com.kostya.habittracker.dto.HabitResponse;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitIcon;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HabitMapper {

	private final HabitAggregateMapper habitAggregateMapper;

	public HabitResponse toResponse(Habit entity) {
		if (entity == null) {
			return null;
		}
		return HabitResponse.builder()
			.id(entity.getId())
			.name(entity.getName())
			.aggregate(habitAggregateMapper.toResponse(entity.getAggregate()))
			.iconId(entity.getIcon() != null ? entity.getIcon().getId() : null)
			.iconUrl(entity.getIcon() != null ? entity.getIcon().getS3Url() : null)
			.build();
	}

	public Habit toEntity(HabitRequest request) {
		Habit entity = new Habit();
		entity.setName(request.getName());
		
		// handle icon
		if (request.getIconId() != null) {
			HabitIcon ref = new HabitIcon();
			ref.setId(request.getIconId());
			entity.setIcon(ref);
		} else {
			entity.setIcon(null);
		}

		return entity;
	}

	public Habit toEntity(Integer id, HabitRequest request) {
		Habit entity = toEntity(request);
		entity.setId(id);
		return entity;
	}
}
