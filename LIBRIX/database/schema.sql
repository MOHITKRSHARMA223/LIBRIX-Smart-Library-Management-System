CREATE DATABASE IF NOT EXISTS librix CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE librix;

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL UNIQUE
);
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    full_name VARCHAR(160) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE
);
CREATE TABLE authors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(160) NOT NULL UNIQUE
);
CREATE TABLE publishers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(160) NOT NULL UNIQUE
);
CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(30) UNIQUE,
    title VARCHAR(240) NOT NULL,
    author_id BIGINT NOT NULL,
    publisher_id BIGINT NULL,
    category_id BIGINT NULL,
    edition VARCHAR(40),
    publication_year SMALLINT,
    language VARCHAR(60),
    description TEXT,
    shelf_number VARCHAR(40),
    total_copies INT NOT NULL DEFAULT 0,
    available_copies INT NOT NULL DEFAULT 0,
    cover_path VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (total_copies >= 0 AND available_copies >= 0 AND available_copies <= total_copies),
    FOREIGN KEY (author_id) REFERENCES authors(id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_books_title (title), INDEX idx_books_category (category_id)
);
CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL UNIQUE,
    full_name VARCHAR(160) NOT NULL,
    email VARCHAR(190) NOT NULL UNIQUE,
    phone VARCHAR(40),
    department VARCHAR(120),
    course VARCHAR(120),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE borrow_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_code CHAR(16) NOT NULL UNIQUE,
    member_id BIGINT NOT NULL,
    borrowed_at DATE NOT NULL,
    due_at DATE NOT NULL,
    returned_at DATE NULL,
    status ENUM('ACTIVE','RETURNED','OVERDUE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(id), INDEX idx_borrow_status (status)
);
CREATE TABLE borrow_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (transaction_id) REFERENCES borrow_transactions(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    UNIQUE KEY uq_transaction_book (transaction_id, book_id)
);
CREATE TABLE reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    status ENUM('WAITING','FULFILLED','CANCELLED','EXPIRED') NOT NULL DEFAULT 'WAITING',
    reserved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    FOREIGN KEY (book_id) REFERENCES books(id), FOREIGN KEY (member_id) REFERENCES members(id),
    UNIQUE KEY uq_active_reservation (book_id, member_id, status)
);
CREATE TABLE fines (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    waived_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status ENUM('PENDING','PARTIAL','PAID','WAIVED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES borrow_transactions(id), CHECK (amount >= 0 AND waived_amount >= 0)
);
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fine_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    received_by BIGINT NOT NULL,
    reference_code VARCHAR(60) NOT NULL UNIQUE,
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fine_id) REFERENCES fines(id), FOREIGN KEY (received_by) REFERENCES users(id), CHECK (amount > 0)
);
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(180) NOT NULL,
    message TEXT NOT NULL,
    read_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id), INDEX idx_notifications_user (user_id, read_at)
);
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(80) NOT NULL,
    entity_id BIGINT NULL,
    details JSON,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE system_settings (
    setting_key VARCHAR(100) PRIMARY KEY,
    setting_value VARCHAR(500) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
