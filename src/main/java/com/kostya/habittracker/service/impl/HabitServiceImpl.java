package com.kostya.habittracker.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.dto.HabitRequest;
import com.kostya.habittracker.dto.HabitResponse;
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
	public List<HabitResponse> getHabits(User user) {
		List<Habit> entities = this.habitRepository.findAllByUserId(user.getId());
		
		List<HabitResponse> result = new ArrayList<>();
		for (Habit entity: entities) {
			result.add(habitMapper.toResponse(entity));
		}
		
		return result;
	}

	@Override
	public HabitResponse getHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findWithAggregateByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));
		
		return habitMapper.toResponse(entity);
	}

	@Override
	public HabitResponse createHabit(HabitRequest request, User user) {
		Habit entity = habitMapper.toEntity(request);
		entity.setUser(user);
		
		return habitMapper.toResponse(this.habitRepository.save(entity));
	}

	@Override
	public HabitResponse updateHabit(Integer id, HabitRequest request, User user) {
		Habit entity = habitMapper.toEntity(id, request);
		entity.setUser(user);
		
		return habitMapper.toResponse(this.habitRepository.save(entity));
	}

	@Override
	public void deleteHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));

		this.habitRepository.delete(entity);
	}

	
	
}
