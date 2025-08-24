package com.kostya.habittracker.dto;

import lombok.Data;

@Data
public class HabitStat {

    private Integer habitId;

    private String habitName;

    private Integer done;

    private Integer of;
}
