CREATE DATABASE IF NOT EXISTS librix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE librix;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(500)
);
CREATE TABLE IF NOT EXISTS authors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(160) NOT NULL UNIQUE,
    country VARCHAR(80),
    birth_date DATE,
    biography VARCHAR(2000)
);
CREATE TABLE IF NOT EXISTS publishers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(160) NOT NULL UNIQUE,
    website VARCHAR(190),
    address VARCHAR(500),
    contact VARCHAR(80)
);
CREATE TABLE IF NOT EXISTS books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(30) UNIQUE,
    title VARCHAR(240) NOT NULL,
    author VARCHAR(160) NOT NULL,
    category VARCHAR(120),
    total_copies INT NOT NULL DEFAULT 0,
    available_copies INT NOT NULL DEFAULT 0,
    shelf_location VARCHAR(80),
    CHECK (total_copies >= 0 AND available_copies >= 0 AND available_copies <= total_copies)
);
CREATE TABLE IF NOT EXISTS members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(160) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    user_id BIGINT UNIQUE,
    phone VARCHAR(40),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS loans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    borrowed_at DATE NOT NULL,
    due_at DATE NOT NULL,
    returned_at DATE,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    INDEX idx_loans_status (status)
);
CREATE TABLE IF NOT EXISTS book_reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    requested_at DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    INDEX idx_reservations_status (status),
    INDEX idx_reservations_member (member_id)
);
CREATE TABLE IF NOT EXISTS activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(80) NOT NULL,
    module VARCHAR(80) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS system_notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(180) NOT NULL,
    message VARCHAR(2000) NOT NULL,
    category VARCHAR(40) NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS library_settings (
    setting_key VARCHAR(100) PRIMARY KEY,
    setting_value VARCHAR(1000) NOT NULL
);
CREATE TABLE IF NOT EXISTS wishlist_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_wishlist_user_book (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
CREATE TABLE IF NOT EXISTS reading_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    percent_complete INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_progress_user_book (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
CREATE TABLE IF NOT EXISTS study_resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(180) NOT NULL,
    resource_type VARCHAR(80) NOT NULL,
    description VARCHAR(500) NOT NULL,
    url VARCHAR(500) NOT NULL
);
CREATE TABLE IF NOT EXISTS user_notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(180) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS study_plan_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(180) NOT NULL,
    scheduled_date DATE NOT NULL,
    duration_minutes INT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_study_plan_date (scheduled_date)
);
CREATE TABLE IF NOT EXISTS seat_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    seat_label VARCHAR(30) NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_seat_booking_date (booking_date),
    INDEX idx_seat_booking_user (user_id)
);
