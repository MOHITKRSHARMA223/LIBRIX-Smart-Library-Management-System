# Database Design

The schema is normalized around users, members, books, catalog reference tables, borrow transactions, borrow items, reservations, fines, payments, notifications, and audit logs. Foreign keys preserve referential integrity; unique constraints prevent duplicate usernames, emails, ISBNs, references, and active transaction-book pairs. Monetary values use `DECIMAL(10,2)`.
