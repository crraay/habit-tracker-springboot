package com.kostya.habittracker.mapper;

import org.springframework.stereotype.Component;

import com.kostya.habittracker.dto.HabitIconDTO;
import com.kostya.habittracker.entity.HabitIcon;

@Component
public class HabitIconMapper {
    
    public HabitIconDTO toResponse(HabitIcon entity) {
        return HabitIconDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .s3Url(entity.getS3Url())
            .isActive(entity.getIsActive())
            .build();
    }
}
