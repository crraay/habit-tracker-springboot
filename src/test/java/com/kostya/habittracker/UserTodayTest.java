package com.kostya.habittracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kostya.habittracker.dto.UserClockResponse;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.BadRequestException;
import com.kostya.habittracker.repository.UserRepository;
import com.kostya.habittracker.service.impl.UserClockServiceImpl;

class UserTodayTest {

    /**
     * Fixed UTC instant that falls on different calendar dates in the two zones:
     * America/New_York (EST, UTC-5) = 2026-01-14 22:00; Asia/Bangkok (UTC+7) = 2026-01-15 10:00.
     */
    static final Instant FIXED_INSTANT = Instant.parse("2026-01-15T03:00:00Z");

    private UserRepository userRepository;
    private UserClockServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Clock clock = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
        service = new UserClockServiceImpl(clock, userRepository);
    }

    @Test
    void todayDiffersForNewYorkAndBangkokAtFixedInstant() {
        User newYork = user("ny", "America/New_York");
        User bangkok = user("bkk", "Asia/Bangkok");

        LocalDate newYorkToday = service.getCurrentUserClock(newYork).getToday();
        LocalDate bangkokToday = service.getCurrentUserClock(bangkok).getToday();

        assertEquals(LocalDate.of(2026, 1, 14), newYorkToday);
        assertEquals(LocalDate.of(2026, 1, 15), bangkokToday);
        assertNotEquals(newYorkToday, bangkokToday);
    }

    @Test
    void afterTimezoneUpdateSubsequentTodayUsesNewZone() {
        User user = user("traveler", "America/New_York");
        assertEquals(LocalDate.of(2026, 1, 14), service.getCurrentUserClock(user).getToday());

        UserClockResponse updated = service.updateTimezone(user, "Asia/Bangkok");
        assertEquals("Asia/Bangkok", updated.getTimezone());
        assertEquals(LocalDate.of(2026, 1, 15), updated.getToday());
        assertEquals(LocalDate.of(2026, 1, 15), service.getCurrentUserClock(user).getToday());
    }

    @Test
    void unknownTimezoneIsRejected() {
        User user = user("traveler", "UTC");
        assertThrows(BadRequestException.class, () -> service.updateTimezone(user, "Not/A/Zone"));
        assertEquals("UTC", user.getTimezone());
    }

    private User user(String username, String timezone) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(username + "@example.com");
        user.setTimezone(timezone);
        return user;
    }
}
