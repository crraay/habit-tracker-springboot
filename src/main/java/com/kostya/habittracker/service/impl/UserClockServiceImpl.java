package com.kostya.habittracker.service.impl;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kostya.habittracker.dto.UserClockResponse;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.BadRequestException;
import com.kostya.habittracker.repository.UserRepository;
import com.kostya.habittracker.service.UserClockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserClockServiceImpl implements UserClockService {

    private final Clock clock;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserClockResponse getCurrentUserClock(User user) {
        return toResponse(user);
    }

    @Override
    @Transactional
    public UserClockResponse updateTimezone(User user, String timezone) {
        ZoneId zone = requireIanaZone(timezone);
        user.setTimezone(zone.getId());
        userRepository.save(user);
        return toResponse(user);
    }

    private UserClockResponse toResponse(User user) {
        ZoneId zone = requireIanaZone(user.getTimezone());
        LocalDate today = LocalDate.now(clock.withZone(zone));
        return UserClockResponse.builder()
            .timezone(user.getTimezone())
            .today(today)
            .build();
    }

    private ZoneId requireIanaZone(String timezone) {
        if (timezone == null || timezone.isBlank() || !ZoneId.getAvailableZoneIds().contains(timezone)) {
            throw new BadRequestException("Unknown timezone: " + timezone);
        }
        try {
            return ZoneId.of(timezone);
        } catch (DateTimeException e) {
            throw new BadRequestException("Unknown timezone: " + timezone);
        }
    }
}
