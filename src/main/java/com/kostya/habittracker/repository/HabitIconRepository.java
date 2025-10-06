package com.kostya.habittracker.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.kostya.habittracker.entity.HabitIcon;

public interface HabitIconRepository extends Repository<HabitIcon, Integer> {

    List<HabitIcon> findAll();
}
