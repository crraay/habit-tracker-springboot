package com.kostya.habittracker.service;

import com.kostya.habittracker.dto.UserClockResponse;
import com.kostya.habittracker.entity.User;

public interface UserClockService {

    UserClockResponse getCurrentUserClock(User user);

    UserClockResponse updateTimezone(User user, String timezone);
}
