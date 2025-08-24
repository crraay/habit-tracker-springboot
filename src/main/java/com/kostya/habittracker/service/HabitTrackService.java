package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;

import com.kostya.habittracker.dto.HabitTrackRequest;
import com.kostya.habittracker.dto.HabitTrackResponse;
import com.kostya.habittracker.entity.User;

public interface HabitTrackService {

    List<HabitTrackResponse> getTrackingList(LocalDate date, User user);

    void trackHabit(HabitTrackRequest trackRequest, User user);

    void untrackHabit(HabitTrackRequest trackRequest, User user);
}
