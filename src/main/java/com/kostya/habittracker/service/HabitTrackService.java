package com.kostya.habittracker.service;

import java.time.LocalDate;
import java.util.List;

import com.kostya.habittracker.model.HabitTrackRequest;
import com.kostya.habittracker.model.HabitTrackResponse;

public interface HabitTrackService {

    List<HabitTrackResponse> getTrackingList(LocalDate date);

    void trackHabit(HabitTrackRequest trackRequest);

    void untrackHabit(HabitTrackRequest trackRequest);
}
