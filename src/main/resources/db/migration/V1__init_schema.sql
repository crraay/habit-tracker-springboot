-- Create user table
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create habit table
CREATE TABLE habit (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    streak INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_habit_user FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

-- Create habit_log table
CREATE TABLE habit_log (
    id SERIAL PRIMARY KEY,
    habit_id INTEGER NOT NULL,
    date DATE NOT NULL,
    CONSTRAINT fk_habit_log_habit FOREIGN KEY (habit_id) REFERENCES habit(id) ON DELETE CASCADE,
    CONSTRAINT unique_habit_date UNIQUE (habit_id, date)
); 