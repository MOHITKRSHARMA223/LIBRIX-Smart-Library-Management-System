# LIBRIX Smart Library Management System

**Smart Library. Smarter Learning.**

LIBRIX is a premium university library platform built with Java 26, Spring
Boot, Thymeleaf, Spring Security, JPA, MySQL, and CSS. It provides separate
student and staff portals for library discovery, circulation, account
management, study planning, seat reservations, notifications, and
administration.

## Technology

- Java 26
- Spring Boot 4.0.0
- Maven
- Spring MVC and Thymeleaf
- Spring Security with BCrypt password hashing
- Spring Data JPA and Hibernate
- MySQL 8 for production
- H2 file database for local development and tests
- JUnit 5, Spring MVC Test, and Spring Security Test
- CSS-only responsive UI

## Strict zero-JavaScript policy

The application contains no JavaScript, `<script>` tags, `.js` files, or
`javascript:` URLs under `src/main`.

All interactions use server-side Spring MVC and Thymeleaf forms. UI behavior
such as animations, hover states, responsive layouts, reduced-motion support,
and password show/hide controls is implemented with CSS and HTML controls.

## Implemented features

### Shared platform

- Premium dark navy, indigo, and purple glass interface
- Original LIBRIX SVG logo, icon, and library illustration assets
- Responsive layouts for desktop, tablet, and mobile
- CSS page transitions, animated cards, hover effects, focus states, and
  reduced-motion support
- High-contrast text and dark hover/focus states across portal tables
- Non-editable surfaces use the default cursor; form controls retain the text
  cursor
- CSRF protection on all state-changing forms
- Server-side validation and persistence
- MySQL-compatible schema and seed data
- H2 local fallback without requiring a separate database server

### Authentication and account management

- Separate student, administrator, and staff login entry points
- Role-aware authentication success routing
- BCrypt password hashing
- Student self-registration at `/student/register`
- Pending student accounts requiring administrative approval
- Student account activation and suspension
- Registration and account activity audit records
- In-app user notifications for account status changes
- CSS-only show/hide password eye controls on:
  - Student login
  - Administrator login
  - Staff login
  - Student registration password
  - Student registration confirmation password
- Passwords remain masked by default and form field names/bindings are
  unchanged

### Student portal

- Personalized student dashboard
- Borrowed-book, completed-book, reservation, overdue, and notification
  metrics
- Category-based book recommendations
- Server-side catalog search
- My library view with loans, reservations, wishlist, and reading progress
- Book reservations/waitlists for unavailable books
- Reservation cancellation
- Reading progress tracking from 0 to 100 percent
- Reading history
- One-time seven-day loan renewal for active, non-overdue loans without an
  active waitlist
- Wishlist management
- Study resources and external academic links
- Personal notification center
- Study planner with:
  - Future-date validation
  - 15-to-480-minute duration validation
  - Completion/reopen workflow
  - Deletion
  - Admin visibility
- Library seat booking with:
  - Seat label and date/time selection
  - 07:00 to 22:00 booking window
  - End-time validation
  - Overlap prevention
  - Cancellation
  - Admin visibility
- Book community reviews with one-review-per-student-per-book validation
- XP and achievement badges earned from verified community participation
- Student exchange and donation listings with owner/admin closure controls
- Research paper repository with authenticated submissions
- Server-side library assistant for catalog search and borrowing/seat guidance
- Current occupancy dashboard based on confirmed seat bookings

### Admin and staff portal

- Separate administration dashboard and navigation
- Operations dashboard with due-soon (next three days) and overdue-loan alerts
- Dashboard metrics for users, books, loans, reservations, study plans, and
  seat bookings
- Book inventory management
- Member management
- Borrowing and return processing
- Categories, authors, and publishers management
- Reference-safe catalog deletion
- Global server-side search
- Reservation queue management
- Reservation approval/ready and cancellation workflows
- Student account approval and suspension
- Activity log monitoring
- System notification management
- Library settings management
- Study-plan oversight
- Seat-booking oversight
- Overdue and fine reporting
- Downloadable overdue-loan CSV report
- Student book reviews and ratings with one review per student/book
- XP gamification and achievement badges
- Student book exchange and donation listings
- Research paper repository with authenticated submissions
- Server-side library assistant for catalog and rules questions
- Student occupancy dashboard based on confirmed seat bookings
- Admin community moderation for reviews, exchanges, and research papers
- Admin analytics dashboard for operational statistics

### New routes

| Area | URL | Access |
|---|---|---|
| Student community | `/student/community` | `STUDENT` |
| Book exchange | `/student/exchange` | `STUDENT` |
| Research repository | `/student/research` | `STUDENT` |
| Library assistant | `/student/assistant` | `STUDENT` |
| Occupancy dashboard | `/student/occupancy` | `STUDENT` |
| Community moderation | `/admin/community` | `SUPER_ADMIN` or `LIBRARIAN` |
| Analytics | `/admin/analytics` | `SUPER_ADMIN` or `LIBRARIAN` |

### Latest verification

- 22 automated tests passed
- Maven package build succeeds
- Authenticated student dashboard returns HTTP 200
- Student community, assistant, occupancy, and admin login return HTTP 200
- Existing H2 loan records remain compatible with the renewal-count migration
- Community moderation for reviews, exchanges, and research submissions
- Operations analytics for catalog, loans, reservations, seats, and study plans

## Portal entry points

| Area | URL | Access |
|---|---|---|
| Student sign in | `/student/login` | Public |
| Student registration | `/student/register` | Public |
| Admin sign in | `/admin/login` | Public |
| Staff sign in | `/login` | Redirects to the student login entry point |
| Student dashboard | `/student/dashboard` | `STUDENT` |
| Student catalog | `/student/catalog` | `STUDENT` |
| Student library | `/student/my-books` | `STUDENT` |
| Study planner | `/student/planner` | `STUDENT` |
| Seat booking | `/student/seats` | `STUDENT` |
| Study resources | `/student/resources` | `STUDENT` |
| Student notifications | `/student/notifications` | `STUDENT` |
| Student community | `/student/community` | `STUDENT` |
| Student exchange | `/student/exchange` | `STUDENT` |
| Student research repository | `/student/research` | `STUDENT` |
| Student library assistant | `/student/assistant` | `STUDENT` |
| Student occupancy | `/student/occupancy` | `STUDENT` |
| Admin dashboard | `/admin/dashboard` | `SUPER_ADMIN` or `LIBRARIAN` |
| Books | `/books` | `SUPER_ADMIN` or `LIBRARIAN` |
| Members | `/members` | `SUPER_ADMIN` or `LIBRARIAN` |
| Borrowing | `/borrowing` | `SUPER_ADMIN` or `LIBRARIAN` |
| Admin reservations | `/admin/reservations` | `SUPER_ADMIN` |
| Student accounts | `/admin/users` | `SUPER_ADMIN` |
| Study-plan oversight | `/admin/study-plans` | `SUPER_ADMIN` |
| Seat-booking oversight | `/admin/seat-bookings` | `SUPER_ADMIN` |
| Activity logs | `/admin/activity` | `SUPER_ADMIN` |
| Notifications | `/admin/notifications` | `SUPER_ADMIN` |
| Library settings | `/admin/settings` | `SUPER_ADMIN` |
| Reports | `/admin/reports` | `SUPER_ADMIN` or `LIBRARIAN` |
| Community moderation | `/admin/community` | `SUPER_ADMIN` or `LIBRARIAN` |
| Analytics | `/admin/analytics` | `SUPER_ADMIN` or `LIBRARIAN` |
| Overdue CSV | `/admin/reports/overdue.csv` | `SUPER_ADMIN` or `LIBRARIAN` |

## Role boundaries

- `/student/**` requires `ROLE_STUDENT`.
- `/admin/**` requires `ROLE_SUPER_ADMIN` or `ROLE_LIBRARIAN` at the route
  level.
- Account approval, suspension, settings, notifications, activity logs,
  reservation administration, study-plan oversight, and seat-booking
  oversight require `ROLE_SUPER_ADMIN`.
- Books, members, borrowing, catalog references, search, dashboard, and
  reports are available to `SUPER_ADMIN` and `LIBRARIAN`.
- Students can only view and change their own wishlist, reading progress,
  planner items, notifications, reservations, and seat bookings.

## Run locally

Run all Maven commands from the `LIBRIX` directory (the Git repository contains
the application as a nested Maven module).

### Requirements

- JDK 26
- Maven 3.9 or newer
- MySQL 8 if using MySQL instead of the local H2 database

### H2 development mode

The default configuration uses a file-based H2 database at
`./data/librix`.

```powershell
Set-Location .\LIBRIX
mvn clean test
mvn spring-boot:run
```

Open:

```text
http://localhost:8080/student/login
http://localhost:8080/admin/login
```

The default server port is `8080`. Override it with `LIBRIX_PORT`.

Verify that the local server is responding:

```powershell
Invoke-WebRequest http://localhost:8080/student/login -UseBasicParsing
```

The local hot run has been verified from the `LIBRIX` directory. The student
login, admin login, and student registration pages each respond with HTTP 200.

For a packaged run instead of the hot Maven run:

```powershell
mvn package
java -jar target/librix-1.0.0.jar
```

If the file-based H2 database is locked by another local process, stop that
process or use an isolated in-memory database for a temporary run:

```powershell
$env:LIBRIX_DB_URL = "jdbc:h2:mem:librix-local;DB_CLOSE_DELAY=-1"
$env:LIBRIX_DDL_AUTO = "create-drop"
mvn spring-boot:run
```

### MySQL mode

1. Create a MySQL 8 database.
2. Run `database/schema.sql`.
3. Run `database/seed.sql` if demo data is required.
4. Configure the database environment variables.
5. Start the application.

```powershell
$env:LIBRIX_DB_URL = "jdbc:mysql://localhost:3306/librix"
$env:LIBRIX_DB_USER = "librix_user"
$env:LIBRIX_DB_PASSWORD = "change-me"
java -jar target/librix-1.0.0.jar
```

The application reads these properties:

| Environment variable | Default | Purpose |
|---|---|---|
| `LIBRIX_PORT` | `8080` | HTTP server port |
| `LIBRIX_DB_URL` | `jdbc:h2:file:./data/librix` | JDBC URL |
| `LIBRIX_DB_USER` | `sa` | Database username |
| `LIBRIX_DB_PASSWORD` | empty | Database password |
| `LIBRIX_DDL_AUTO` | `update` | Hibernate schema mode |

For production, use a managed migration process and avoid relying on
`LIBRIX_DDL_AUTO=update`.

## Demo credentials

The local development seed creates the following accounts:

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Super administrator |
| `librarian` | `librarian123` | Librarian |
| `student` | `student123` | Student |
| `aisha` | `aisha123` | Student |
| `rohan` | `rohan123` | Student |

These credentials are for local development only. Set
`LIBRIX_DEMO_ADMIN_PASSWORD` and `LIBRIX_DEMO_STUDENT_PASSWORD` before
initializing a fresh local database, and replace all demo credentials before
deployment.

## Database model

The canonical MySQL schema is in `database/schema.sql`. It includes tables for:

- Users and roles
- Members
- Books
- Categories
- Authors
- Publishers
- Loans
- Book reservations
- Wishlist items
- Reading progress
- Study resources
- User notifications
- Study-plan items
- Seat bookings
- Activity logs
- System notifications
- Library settings

The H2 database file under `data/` is local runtime state and may change when
the application runs.

## Testing

Run the complete test suite:

```powershell
mvn test
```

The current suite covers:

- Spring application context loading
- Authentication and role-protected routes
- Student and admin portal access boundaries
- Planner and seat-booking route protection
- Domain validation
- Borrowing rules
- MVC and Spring Security integration behavior

Build without rerunning tests:

```powershell
mvn package -DskipTests
```

## Project layout

```text
src/main/java/com/librix/
  config/          Security and development/demo data configuration
  controller/      Student, admin, catalog, borrowing, report, and auth routes
  model/           JPA entities and enums
  repository/      Spring Data repositories
  service/         Borrowing and reservation business workflows

src/main/resources/
  templates/       Thymeleaf student, admin, auth, catalog, and inventory pages
  static/css/      Student, admin, and shared premium styles
  static/images/   LIBRIX SVG branding and library artwork
  application.properties

database/
  schema.sql       Canonical MySQL schema
  seed.sql         Demo/reference data

src/test/
  Java integration, validation, security, and business-rule tests
```

## Design and accessibility notes

- The interface uses high-contrast dark surfaces and readable light text.
- Hover and focus states preserve contrast instead of switching to pale
  backgrounds.
- Inputs retain visible focus indicators.
- Password fields are masked on initial render.
- Eye controls have accessible labels and keyboard-focus styling.
- `prefers-reduced-motion` reduces animation and transition duration.
- No client-side JavaScript is required for application workflows.

## Production checklist

- Use MySQL 8 with a dedicated application user.
- Set strong non-demo passwords and secrets.
- Set `LIBRIX_DDL_AUTO=validate` after the schema is provisioned.
- Run behind HTTPS.
- Configure backups for the MySQL database.
- Review and restrict database/network access.
- Remove or replace all demo accounts and sample data.
- Review external study-resource URLs before deployment.
- Run `mvn clean test` and a production package build before release.
