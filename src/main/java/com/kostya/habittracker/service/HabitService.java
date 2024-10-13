package com.kostya.habittracker.service;

import java.util.List;

import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.model.HabitRequest;
import com.kostya.habittracker.model.HabitResponse;

public interface HabitService {
	
	List<HabitResponse> getHabits(User user);
	
	HabitResponse getHabit(Integer id, User user);
	
	HabitResponse createHabit(HabitRequest request, User user);
	
	HabitResponse updateHabit(Integer id, HabitRequest request, User user);
	
	void deleteHabit(Integer id, User user);
	
}
