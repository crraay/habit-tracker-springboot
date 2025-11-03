package com.kostya.habittracker.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kostya.habittracker.dto.HabitIconDTO;
import com.kostya.habittracker.mapper.HabitIconMapper;
import com.kostya.habittracker.repository.HabitIconRepository;
import com.kostya.habittracker.service.HabitIconService;

@Service
public class HabitIconServiceImpl implements HabitIconService {

    @Autowired
    HabitIconRepository habitIconRepository;

    @Autowired
    HabitIconMapper habitIconMapper;
    
    @Override
    public List<HabitIconDTO> getAll() {
        return habitIconRepository.findAll().stream()
            .map(habitIconMapper::toDTO)
            .collect(Collectors.toList());
    }
}
