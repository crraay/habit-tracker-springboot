package com.kostya.habittracker.service;

import java.util.List;

import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;

public interface HabitService {
	
	List<HabitResponse> getHabits();
	
	HabitResponse getHabit(Integer id);
	
	HabitResponse createHabit(HabitRequest request);
	
	HabitResponse updateHabit(Integer id, HabitRequest request);
	
	void deleteHabit(Integer id);
	
}
