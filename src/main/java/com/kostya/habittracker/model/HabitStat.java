package com.kostya.habittracker.model;

import lombok.Data;

@Data
public class HabitStat {

    private Integer habitId;

    private String habitName;

    private Integer done;

    private Integer of;
}
