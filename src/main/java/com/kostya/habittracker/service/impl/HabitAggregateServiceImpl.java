package com.kostya.habittracker.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kostya.habittracker.entity.Habit;
import com.kostya.habittracker.entity.HabitAggregate;
import com.kostya.habittracker.entity.HabitLog;
import com.kostya.habittracker.repository.HabitAggregateRepository;
import com.kostya.habittracker.repository.HabitLogRepository;
import com.kostya.habittracker.service.HabitAggregateService;

@Service
public class HabitAggregateServiceImpl implements HabitAggregateService {

	@Autowired
	private HabitAggregateRepository habitAggregateRepository;

	@Autowired
	private HabitLogRepository habitLogRepository;

	@Override
	@Transactional(readOnly = true)
	public HabitAggregate getOrCreateAggregate(Habit habit) {
		HabitAggregate aggregate = habit.getAggregate();
		if (aggregate == null) {
			aggregate = this.habitAggregateRepository.findById(habit.getId()).orElse(null);
		}
		if (aggregate == null) {
			aggregate = new HabitAggregate();
			aggregate.setHabit(habit);
			aggregate.setTotalCheckIns(0);
			aggregate.setCurrentStreak(0);
			aggregate.setBestStreak(0);
			aggregate.setLastCheckInDate(null);
			aggregate.setStreakStartDate(null);
		}
		return aggregate;
	}

	@Override
	@Transactional
	public void applyTrack(Habit habit, LocalDate date) {
		HabitAggregate aggregate = getOrCreateAggregate(habit);
		aggregate.setTotalCheckIns(aggregate.getTotalCheckIns() + 1);

		if (aggregate.getLastCheckInDate() == null) {
			aggregate.setLastCheckInDate(date);
			aggregate.setStreakStartDate(date);
			aggregate.setCurrentStreak(1);
			aggregate.setBestStreak(Math.max(aggregate.getBestStreak(), 1));
		} else if (date.equals(aggregate.getLastCheckInDate().plusDays(1))) {
			aggregate.setLastCheckInDate(date);
			aggregate.setCurrentStreak(aggregate.getCurrentStreak() + 1);
			aggregate.setBestStreak(Math.max(aggregate.getBestStreak(), aggregate.getCurrentStreak()));
		} else if (date.isAfter(aggregate.getLastCheckInDate().plusDays(1))) {
			aggregate.setLastCheckInDate(date);
			aggregate.setStreakStartDate(date);
			aggregate.setCurrentStreak(1);
			// best streak unchanged
		} else {
			// backfill: recompute to ensure correctness
			recomputeAggregate(habit);
			return;
		}

		this.habitAggregateRepository.save(aggregate);
	}

	@Override
	@Transactional
	public void applyUntrack(Habit habit, LocalDate date) {
		// safest default: recompute entirely
		recomputeAggregate(habit);
	}

	@Override
	@Transactional
	public void recomputeAggregate(Habit habit) {
		HabitAggregate aggregate = getOrCreateAggregate(habit);
		List<HabitLog> logs = this.habitLogRepository.findByHabitOrderByDateDesc(habit);
		int total = logs.size();
		aggregate.setTotalCheckIns(total);

		if (total == 0) {
			aggregate.setCurrentStreak(0);
			aggregate.setBestStreak(0);
			aggregate.setLastCheckInDate(null);
			aggregate.setStreakStartDate(null);
			this.habitAggregateRepository.save(aggregate);
			return;
		}

		// compute segments from newest to oldest
		int best = 1;
		int currentLen = 1;
		LocalDate last = logs.get(0).getDate();

		for (int i = 1; i < logs.size(); i++) {
			LocalDate d = logs.get(i).getDate();
			if (d.plusDays(1).equals(last)) {
				// contiguous
				currentLen++;
			} else {
				// close previous segment
				if (currentLen > best) best = currentLen;
				// start new segment
				last = d;
				currentLen = 1;
				continue;
			}
			last = d;
		}
		if (currentLen > best) best = currentLen;

		// the first segment in desc order is the current one
		aggregate.setLastCheckInDate(logs.get(0).getDate());
		// rebuild current segment start by scanning forward from most recent until gap
		LocalDate curStart = logs.get(0).getDate();
		LocalDate prev = curStart;
		for (int i = 1; i < logs.size(); i++) {
			LocalDate d = logs.get(i).getDate();
			if (d.plusDays(1).equals(prev)) {
				curStart = d;
				prev = d;
			} else {
				break;
			}
		}
		int current = (int) (aggregate.getLastCheckInDate().toEpochDay() - curStart.toEpochDay()) + 1;
		aggregate.setStreakStartDate(curStart);
		aggregate.setCurrentStreak(current);
		aggregate.setBestStreak(best);

		this.habitAggregateRepository.save(aggregate);
	}

	@Override
	@Transactional
	public void resetStreaksForUncheckedHabits(LocalDate today) {
		List<HabitAggregate> aggregatesToReset = this.habitAggregateRepository
				.findAggregatesNeedingStreakReset(today);

		for (HabitAggregate aggregate : aggregatesToReset) {
			aggregate.setCurrentStreak(0);
			aggregate.setStreakStartDate(null);
			this.habitAggregateRepository.save(aggregate);
		}
	}
}


