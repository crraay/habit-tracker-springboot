package com.kostya.habittracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;
import com.kostya.habittracker.repository.HabitRepository;

@Service
public class HabitServiceImpl implements HabitService {

	@Autowired
	HabitRepository habitRepository;
	

	@Override
	public List<HabitResponse> getHabits() {
		List<Habit> entities = this.habitRepository.findAll();
		
		List<HabitResponse> result = new ArrayList<>();
		for (Habit entity: entities) {
			result.add(HabitResponse.convert(entity));
		}
		
		return result;
	}

	@Override
	public HabitResponse getHabit(Integer id) {
		Habit entity = this.habitRepository.findById(id).get();
		
		return HabitResponse.convert(entity);
	}

	@Override
	public HabitResponse createHabit(HabitRequest request) {
		Habit entity = this.habitRepository.save(request.convert());
		
		return HabitResponse.convert(entity);
	}

	@Override
	public HabitResponse updateHabit(Integer id, HabitRequest request) {
		Habit entity = this.habitRepository.save(request.convert(id));
		
		return HabitResponse.convert(entity);
	}

	@Override
	public void deleteHabit(Integer id) {
		this.habitRepository.deleteById(id);
	}

	
	
}
