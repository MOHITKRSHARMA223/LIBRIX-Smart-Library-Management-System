# LIBRIX

**Smart Library. Smarter Learning.**

LIBRIX is a Java 26 Spring Boot library-management application with server-rendered Thymeleaf pages, CSS-only responsive design, JPA persistence, and role-aware Spring Security authentication.

## Current implementation

- Java 26 and Spring Boot 4 Maven project configuration
- Thymeleaf HTML and CSS-only responsive interface with no JavaScript
- BCrypt password hashing and database-backed role-aware authentication
- JPA repositories and transactional borrowing/return workflows
- Dashboard counts sourced from the persistence layer
- Book search, add/delete, member registration, and availability tracking
- Categories, authors, and publishers management with reference-safe deletion
- Global server-side search across catalog and member records
- Super-admin activity logs, system notifications, and library settings
- Separate student and admin portals with distinct login pages, layouts, dashboards, and role-enforced URL namespaces
- Student self-service waitlists for unavailable books, with cancellation and reservation status tracking
- Admin reservation queue, overdue/fine reporting, and downloadable overdue-loan CSV exports
- Explicit role boundaries for circulation, catalog management, search, reports, and administration
- MySQL schema plus H2 local fallback for development
- JUnit 5 and Spring MVC integration tests

The application uses H2 file storage when MySQL is not configured. Configure `LIBRIX_DB_URL`, `LIBRIX_DB_USER`, and `LIBRIX_DB_PASSWORD` for MySQL 8.

## Run

```powershell
mvn clean test
mvn package
java -jar target/librix-1.0.0.jar
```

Open http://localhost:8080/login.

Portal entry points are `/student/login` and `/admin/login`. Students are routed to the student portal after authentication; staff are routed to the admin portal.

The current machine has JDK 26.0.2 installed. Maven must be installed or its `bin` directory added to `PATH` before running the commands above.

## Demo credentials

The local demo seed creates these accounts:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Super administrator |
| `librarian` | `librarian123` | Librarian |
| `student` | `student123` | Student |
| `aisha` | `aisha123` | Student |
| `rohan` | `rohan123` | Student |

These are demo-only credentials. Set `LIBRIX_DEMO_ADMIN_PASSWORD` and
`LIBRIX_DEMO_STUDENT_PASSWORD` before starting a fresh local database, and replace
demo credentials before deploying to production.

## Database setup

1. Create a MySQL 8 database.
2. Run `database/schema.sql`, then `database/seed.sql`.
3. Set the three `LIBRIX_DB_*` environment variables.
4. Start LIBRIX.

See [docs/installation.md](docs/installation.md) and [docs/testing.md](docs/testing.md).

For an existing MySQL installation, apply `database/migrations/V002__book_reservations.sql` before using student waitlists. Hibernate `update` will also create this additive table during local development.

## Honest scope note

The current web application is implemented and runnable for authentication, dashboard, books, members, borrowing, catalog references, global search, and core administration. Reservations, payments, report exports, and several advanced workflows remain tracked in [PROGRESS.md](PROGRESS.md) and are not presented as complete.
