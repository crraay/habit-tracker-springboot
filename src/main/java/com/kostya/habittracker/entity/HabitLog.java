package com.kostya.habittracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"habit_id", "date"})
})
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    @ManyToOne
    Habit habit;
    
    LocalDate date;
}