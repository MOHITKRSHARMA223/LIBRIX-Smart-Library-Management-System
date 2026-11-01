USE librix;
INSERT INTO categories (name, description) VALUES ('Computer Science', 'Programming, systems, and software engineering'), ('Literature', 'Classic and contemporary fiction'), ('Personal Development', 'Habits, learning, and personal growth') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO authors (name) VALUES ('Robert C. Martin'), ('James Clear'), ('Jane Austen') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO publishers (name) VALUES ('Prentice Hall'), ('Avery'), ('Penguin Classics') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO library_settings (setting_key, setting_value) VALUES ('policy.borrowing-days', '14'), ('policy.daily-fine', '2.00') ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value);
-- Password hashes and accounts are provisioned by the application seed or the registration/approval workflow.
