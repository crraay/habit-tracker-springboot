package com.kostya.habittracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.enums.UserStatus;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;
import com.kostya.habittracker.repository.UserRepository;
import com.kostya.habittracker.service.UserClockService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TimezoneUpdateLeavesLogDatesTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private UserClockService userClockService;

    @Test
    void updatingTimezoneDoesNotRewriteHabitLogDate() {
        User user = new User();
        user.setUsername("log-date-user");
        user.setPassword("secret");
        user.setEmail("log-date-user@example.com");
        user.setStatus(UserStatus.ACTIVE);
        user.setTimezone("UTC");
        user = userRepository.save(user);

        Habit habit = new Habit();
        habit.setUser(user);
        habit.setName("Read");
        habit = habitRepository.save(habit);

        LocalDate storedDate = LocalDate.of(2026, 1, 10);
        HabitLog log = new HabitLog();
        log.setHabit(habit);
        log.setDate(storedDate);
        habitLogRepository.save(log);

        userClockService.updateTimezone(user, "Asia/Bangkok");

        List<HabitLog> logs = habitLogRepository.findByHabitOrderByDateDesc(habit);
        assertEquals(1, logs.size());
        assertNotNull(logs.get(0).getId());
        assertEquals(storedDate, logs.get(0).getDate());
    }
}
