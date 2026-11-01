CREATE TABLE IF NOT EXISTS book_reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    requested_at DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    CONSTRAINT fk_reservation_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES members(id),
    INDEX idx_reservations_status (status),
    INDEX idx_reservations_member (member_id)
);
