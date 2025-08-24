package com.kostya.habittracker.dto;

import lombok.Data;

@Data
public class HabitStatRequest {

    private Integer habitId;

    private String habitName;

    private Integer done;

    private Integer of;
}
