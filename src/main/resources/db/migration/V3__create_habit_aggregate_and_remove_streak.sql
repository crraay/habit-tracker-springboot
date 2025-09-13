-- Create habit_aggregate table to store denormalized habit metrics
CREATE TABLE public.habit_aggregate (
    habit_id INTEGER PRIMARY KEY,
    total_check_ins INTEGER NOT NULL DEFAULT 0,
    current_streak INTEGER NOT NULL DEFAULT 0,
    best_streak INTEGER NOT NULL DEFAULT 0,
    last_check_in_date DATE NULL,
    streak_start_date DATE NULL,
    -- audit fields (align with BasicAudit)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    modified_by VARCHAR(255) NOT NULL DEFAULT 'system',
    CONSTRAINT fk_habit_aggregate_habit FOREIGN KEY (habit_id) REFERENCES public.habit(id) ON DELETE CASCADE
);

-- Index to accelerate queries filtering/ordering by recent activity
CREATE INDEX idx_habit_aggregate_last_check_in_date
    ON public.habit_aggregate(last_check_in_date);

-- Remove deprecated streak column from habit table (moved to aggregate)
ALTER TABLE public.habit
    DROP COLUMN IF EXISTS streak;


