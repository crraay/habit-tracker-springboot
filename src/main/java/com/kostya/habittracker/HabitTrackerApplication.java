package com.kostya.habittracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class HabitTrackerApplication {

	public static void main(String[] args) {
		log.info("Starting Habit Tracker application.");
		SpringApplication.run(HabitTrackerApplication.class, args);
	}

}
