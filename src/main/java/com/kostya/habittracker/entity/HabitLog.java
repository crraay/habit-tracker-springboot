package com.kostya.habittracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"habit_id", "date"})
})
@EqualsAndHashCode(callSuper = true)
public class HabitLog extends BasicAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Habit habit;
    
    @Column(nullable = false)
    LocalDate date;
}