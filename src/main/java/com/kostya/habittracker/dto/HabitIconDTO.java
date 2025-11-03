package com.kostya.habittracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HabitIconDTO {
    
    private Integer id;
    private String name;
    private String description;
    private String s3Url;
    private Boolean isActive;
}
