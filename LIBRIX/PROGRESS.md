# LIBRIX Progress

Updated: 2026-11-01

## Completed

- Java 26 and Spring Boot 4.0.0 Maven build
- Spring MVC, Thymeleaf, Spring Security, JPA, validation
- MySQL environment configuration with H2 local fallback
- BCrypt password hashing and database-backed role-aware authentication
- CSS-only responsive login, dashboard, books, members, and borrowing pages
- Database-backed dashboard counts and recent loans
- Book search, add, delete, availability tracking
- Member add and list workflows
- Transactional borrow and return workflow with 14-day / three-loan rules
- 11 automated tests, including Spring context and MockMvc security tests
- Zero JavaScript files and zero script tags verified
- Packaged JAR and HTTP smoke test on port 8080
- Categories, authors, and publishers management with search and deletion guards
- Global server-side search across catalog and member records
- Super-admin activity log, notification, and library settings pages
- Database-backed advanced seed records and 10 JPA repositories

## Verified

- `mvn test package`: BUILD SUCCESS
- 13 tests passed
- `GET /login`: HTTP 200
- Running on Java 26.0.2
- Updated server starts on port 8080
- Separate student and admin login experiences
- Role-enforced `/student/**` and `/admin/**` namespaces
- Separate student dashboard/catalog/my-library layout and CSS
- Separate admin dashboard/navigation layout and CSS
- Shared student member profile mapping for private loan history
- Cross-portal MVC access tests and role-denial tests

## Remaining

- Reservation queue and expiry workflow
- Book acquisition and supplier records
- Shelf/rack locations and capacity views
- Lost/damaged book workflow
- Renewal requests and reading lists/history
- Student reservations, renewals, wishlist, reading lists, reviews, and profile editing
- Separate admin acquisitions, inventory, staff, reports, and support workflows
- Recommendations, reviews, staff management, and permissions UI
- CSV import/export, barcode/QR generation, backup/restore, help/support
- Fine/payment entities and staff payment workflow
- Notification center and scheduled due-date notifications
- PDF/CSV/Excel report exports
- Cover image upload and CSV import
- Full member profile and renewal workflow
- MySQL integration test profile and concurrency tests
