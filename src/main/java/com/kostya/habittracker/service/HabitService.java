package com.kostya.habittracker.service;

import java.util.List;

import com.kostya.habittracker.dto.HabitDTO;
import com.kostya.habittracker.entity.User;

public interface HabitService {
	
	List<HabitDTO> getHabits(User user);
	
	HabitDTO getHabit(Integer id, User user);
	
	HabitDTO createHabit(HabitDTO dto, User user);
	
	HabitDTO updateHabit(Integer id, HabitDTO dto, User user);
	
	void deleteHabit(Integer id, User user);
	
}
