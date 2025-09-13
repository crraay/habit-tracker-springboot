package com.kostya.habittracker.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kostya.habittracker.dto.HabitTrackRequest;
import com.kostya.habittracker.dto.HabitTrackResponse;
import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.entity.User;
import com.kostya.habittracker.exception.ConflictException;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.repository.HabitRepository;
import com.kostya.habittracker.service.HabitTrackService;
import com.kostya.habittracker.service.HabitAggregateService;

import lombok.RequiredArgsConstructor;

import com.kostya.habittracker.mapper.HabitTrackMapper;

@Service
@RequiredArgsConstructor
public class HabitTrackServiceImpl implements HabitTrackService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final HabitAggregateService habitAggregateService;
    private final HabitTrackMapper habitTrackMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitTrackResponse> getTrackingList(LocalDate date, User user) {
        List<Habit> habits = this.habitRepository.findAllByUserId(user.getId());
        List<HabitLog> habitLogs = this.habitLogRepository.findByDate(date);

        Set<Integer> doneIds = habitLogs.stream()
            .map(hl -> hl.getHabit().getId())
            .collect(Collectors.toSet());

        return habits.stream()
            .map(habit -> habitTrackMapper.toResponse(habit, doneIds.contains(habit.getId())))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void trackHabit(HabitTrackRequest trackRequest, User user) {
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        if (this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate()) != null) {
            throw new ConflictException("Habit log already exists for the given habit and date");
        }
        
        // create habit log (track)
        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);
        habitLog.setDate(trackRequest.getDate());
        this.habitLogRepository.save(habitLog);

        this.habitAggregateService.applyTrack(habit, trackRequest.getDate());
    }

    @Override
    @Transactional
    public void untrackHabit(HabitTrackRequest trackRequest, User user) {
        // find habit log by habit and date
        Habit habit = this.habitRepository.findByIdAndUserId(trackRequest.getHabitId(), user.getId())
            .orElseThrow(() -> new NotFoundException("Habit not found"));

        HabitLog habitLog = this.habitLogRepository.findByHabitAndDate(habit, trackRequest.getDate());
        if (habitLog == null) {
            throw new ConflictException("Habit log not found for the given habit and date");
        }

        // delete habit log (untrack)
        this.habitLogRepository.delete(habitLog);

        this.habitAggregateService.applyUntrack(habit, trackRequest.getDate());
    }
}
