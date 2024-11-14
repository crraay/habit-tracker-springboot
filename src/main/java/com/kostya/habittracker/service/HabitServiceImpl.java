package com.kostya.habittracker.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;
import com.kostya.habittracker.repository.HabitRepository;

@Service
public class HabitServiceImpl implements HabitService {

	private static final Logger logger = LoggerFactory.getLogger(HabitServiceImpl.class);

	@Autowired
	HabitRepository habitRepository;

	@Override
	public List<HabitResponse> getHabits(User user) {
		List<Habit> entities = this.habitRepository.findAllByUserId(user.getId());
		
		List<HabitResponse> result = new ArrayList<>();
		for (Habit entity: entities) {
			result.add(HabitResponse.convert(entity));
		}
		
		return result;
	}

	@Override
	public HabitResponse getHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));
		
		return HabitResponse.convert(entity);
	}

	@Override
	public HabitResponse createHabit(HabitRequest request, User user) {
		Habit entity = request.convert();
		entity.setUser(user);
		
		return HabitResponse.convert(this.habitRepository.save(entity));
	}

	@Override
	public HabitResponse updateHabit(Integer id, HabitRequest request, User user) {
		Habit entity = request.convert(id);
		entity.setUser(user);
		
		return HabitResponse.convert(this.habitRepository.save(entity));
	}

	@Override
	public void deleteHabit(Integer id, User user) {
		Habit entity = this.habitRepository.findByIdAndUserId(id, user.getId())
			.orElseThrow(() -> new NotFoundException("Habit not found"));

		this.habitRepository.delete(entity);
	}

	
	
}
