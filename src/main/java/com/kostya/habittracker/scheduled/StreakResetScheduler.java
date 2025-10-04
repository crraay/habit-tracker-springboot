package com.kostya.habittracker.scheduled;

import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kostya.habittracker.service.HabitAggregateService;

@Component
public class StreakResetScheduler {

    private static final Logger logger = LoggerFactory.getLogger(StreakResetScheduler.class);

    @Autowired
    private HabitAggregateService habitAggregateService;

    @Value("${app.scheduling.streak-reset.enabled}")
    private boolean streakResetEnabled;

    @Value("${app.scheduling.timezone}")
    private String timezone;

    @Scheduled(cron = "${app.scheduling.streak-reset.cron}", zone = "${app.scheduling.timezone}")
    @Transactional
    public void resetStreaksAtMidnight() {
        if (!streakResetEnabled) {
            logger.debug("Streak reset scheduler is disabled");
            return;
        }

        try {
            LocalDate today = LocalDate.now(ZoneId.of(timezone));
            logger.info("Starting streak reset process for date: {}", today);
            habitAggregateService.resetStreaksForUncheckedHabits(today);
            logger.info("Successfully completed streak reset process for date: {}", today);

        } catch (Exception e) {
            logger.error("Error occurred during streak reset process", e);
        }
    }

    @Transactional
    public void resetStreaksManually() {
        logger.info("Manual streak reset triggered");
        resetStreaksAtMidnight();
    }
}
