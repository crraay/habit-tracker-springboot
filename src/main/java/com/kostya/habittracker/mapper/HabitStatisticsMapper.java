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
        
        HabitStatResponse stats = new HabitStatResponse();
        stats.setHabitId(habit.getId());
        stats.setHabitName(habit.getName());
        stats.setDone(completionCount.intValue());
        stats.setOf(totalDays);
        stats.setAggregate(habitAggregateMapper.toResponse(habit.getAggregate()));
        stats.setIconUrl(habit.getIcon() != null ? habit.getIcon().getS3Url() : null);
        
        return stats;
    }
}
