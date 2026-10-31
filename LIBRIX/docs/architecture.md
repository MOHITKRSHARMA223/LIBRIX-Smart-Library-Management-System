# Architecture

LIBRIX follows a layered MVC shape:

- `model`: immutable domain records and enums.
- `service`: business rules independent of Swing and JDBC.
- `dao`: planned JDBC persistence boundary using prepared statements and explicit transactions.
- `security`: authentication, roles, password hashing, and session state.
- `ui`: Swing frames and reusable components.
- `database`: MySQL schema, constraints, indexes, seed data, and migrations.

Borrowing rules are isolated in `BorrowingRules` so they can be tested without a running database. A production borrow operation must lock the selected book row and member loan count in one transaction before decrementing availability.
