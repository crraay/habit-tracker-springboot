-- Add habit icon support: create habit_icon table and add icon_id to habit table

-- Create habit_icon table
CREATE TABLE public.habit_icon (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    s3_url VARCHAR(500) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NOT NULL DEFAULT 'system',
    modified_by VARCHAR(255) NOT NULL DEFAULT 'system'
);

-- Add icon_id column to habit table
ALTER TABLE public.habit ADD COLUMN icon_id INTEGER;

-- Add foreign key constraint
ALTER TABLE public.habit 
    ADD CONSTRAINT fk_habit_icon 
    FOREIGN KEY (icon_id) REFERENCES public.habit_icon(id) ON DELETE SET NULL;

-- Create indexes
CREATE INDEX idx_habit_icon_id ON public.habit(icon_id);
CREATE INDEX idx_habit_icon_active ON public.habit_icon(is_active);
