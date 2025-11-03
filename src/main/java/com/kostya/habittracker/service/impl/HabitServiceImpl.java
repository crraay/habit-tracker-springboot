package com.kostya.habittracker.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.dto.HabitDTO;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.repository.HabitRepository;
import com.kostya.habittracker.service.HabitService;
import com.kostya.habittracker.mapper.HabitMapper;

@Service
public class HabitServiceImpl implements HabitService {

	@Autowired
	HabitRepository habitRepository;

	@Autowired
	HabitMapper habitMapper;

	@Override
	public List<HabitDTO> getHabits(User user) {
		List<Habit> entities = this.habitRepository.findAllByUserId(user.getId());
		
		List<HabitDTO> result = new ArrayList<>();
		for (Habit entity: entities) {
			result.add(habitMapper.toDTO(entity));
		}
		
		return result;
	}

	@Override
	public HabitDTO getHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findWithAggregateByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));
		
		return habitMapper.toDTO(entity);
	}

	@Override
	public HabitDTO createHabit(HabitDTO dto, User user) {
		Habit entity = habitMapper.toEntity(dto);
		entity.setUser(user);
		
		return habitMapper.toDTO(this.habitRepository.save(entity));
	}

	@Override
	public HabitDTO updateHabit(Integer id, HabitDTO dto, User user) {
		Habit entity = habitMapper.toEntity(id, dto);
		entity.setUser(user);
		
		return habitMapper.toDTO(this.habitRepository.save(entity));
	}

	@Override
	public void deleteHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));

		this.habitRepository.delete(entity);
	}

	
	
}
