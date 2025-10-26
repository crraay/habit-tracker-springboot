package com.kostya.habittracker.mapper;

import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitStatResponse;
import com.kostya.habittracker.entity.Habit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HabitStatisticsMapper {

    private final HabitAggregateMapper habitAggregateMapper;

    public HabitStatResponse toStatResponse(Habit habit, Long completionCount, Integer totalDays) {
        if (habit == null) {
            return null;
        }
        
        return HabitStatResponse.builder()
            .habitId(habit.getId())
            .habitName(habit.getName())
            .done(completionCount.intValue())
            .of(totalDays)
            .aggregate(habitAggregateMapper.toResponse(habit.getAggregate()))
            .iconUrl(habit.getIcon() != null ? habit.getIcon().getS3Url() : null)
            .build();
    }
}
