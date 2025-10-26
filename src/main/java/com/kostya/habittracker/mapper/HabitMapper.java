package com.kostya.habittracker.mapper;

import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitDTO;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitIcon;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HabitMapper {

	private final HabitAggregateMapper habitAggregateMapper;

	public HabitDTO toDTO(Habit entity) {
		if (entity == null) {
			return null;
		}
		return HabitDTO.builder()
			.id(entity.getId())
			.name(entity.getName())
			.aggregate(habitAggregateMapper.toResponse(entity.getAggregate()))
			.iconId(entity.getIcon() != null ? entity.getIcon().getId() : null)
			.iconUrl(entity.getIcon() != null ? entity.getIcon().getS3Url() : null)
			.build();
	}

	public Habit toEntity(HabitDTO dto) {
		Habit entity = new Habit();
		entity.setName(dto.getName());
		
		// handle icon
		if (dto.getIconId() != null) {
			HabitIcon ref = new HabitIcon();
			ref.setId(dto.getIconId());
			entity.setIcon(ref);
		} else {
			entity.setIcon(null);
		}

		return entity;
	}

	public Habit toEntity(Integer id, HabitDTO dto) {
		Habit entity = toEntity(dto);
		entity.setId(id);
		return entity;
	}
}
