-- Add required IANA timezone on public."user". UTC is a migration/new-user default, not a product timezone.

ALTER TABLE public."user" ADD COLUMN timezone VARCHAR(64);

UPDATE public."user"
SET timezone = 'UTC'
WHERE timezone IS NULL;

ALTER TABLE public."user" ALTER COLUMN timezone SET NOT NULL;
