-- Extend public."user" with email, status, role, and auth-related fields

-- 1) Email: add column, backfill from username, enforce NOT NULL and uniqueness
ALTER TABLE public."user" ADD COLUMN email VARCHAR(320);

UPDATE public."user"
SET email = lower(username) || '@example.local'
WHERE email IS NULL;

ALTER TABLE public."user" ALTER COLUMN email SET NOT NULL;

ALTER TABLE public."user" ADD CONSTRAINT uq_user_email UNIQUE (email);

-- 2) Status/Role/Attempts/Timestamps
ALTER TABLE public."user"
    ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    ADD COLUMN role VARCHAR(16) NOT NULL DEFAULT 'USER',
    ADD COLUMN failed_login_attempts INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN last_login_at TIMESTAMP NULL,
    ADD COLUMN password_changed_at TIMESTAMP NULL;

-- 3) Check constraints to keep values aligned with enums
ALTER TABLE public."user"
    ADD CONSTRAINT chk_user_status CHECK (status IN ('PENDING_VERIFICATION','ACTIVE','LOCKED','DELETED')),
    ADD CONSTRAINT chk_user_role CHECK (role IN ('USER','ADMIN'));
