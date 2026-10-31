USE librix;
INSERT INTO roles (name) VALUES ('SUPER_ADMIN'), ('LIBRARIAN'), ('STUDENT') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO categories (name) VALUES ('Computer Science'), ('Literature'), ('Business'), ('Science') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO authors (name) VALUES ('Robert C. Martin'), ('James Clear'), ('Jane Austen') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO publishers (name) VALUES ('Prentice Hall'), ('Avery'), ('Penguin Classics') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO system_settings (setting_key, setting_value) VALUES ('borrowing_days', '14'), ('max_active_loans', '3'), ('daily_fine', '2.00') ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value);
-- Password hashes are intentionally excluded. Create the first administrator through the setup flow.
