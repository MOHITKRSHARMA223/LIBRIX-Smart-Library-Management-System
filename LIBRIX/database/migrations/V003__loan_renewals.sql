ALTER TABLE loans ADD COLUMN IF NOT EXISTS renewal_count INT DEFAULT 0;
UPDATE loans SET renewal_count = 0 WHERE renewal_count IS NULL;
