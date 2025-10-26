package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitIconDTO {
    
    private final Integer id;
    private final String name;
    private final String description;
    private final String s3Url;
    private final Boolean isActive;
}
