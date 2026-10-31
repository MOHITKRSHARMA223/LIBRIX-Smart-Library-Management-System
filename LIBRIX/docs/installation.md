# LIBRIX Installation

## Requirements

- JDK 26.0.2 or newer Java 26 release
- Maven 3.9+
- MySQL 8.0+

## Build

From the `LIBRIX` directory:

```powershell
mvn clean test
mvn package
java -jar target/librix-1.0.0.jar
```

## MySQL

Run `database/schema.sql` and then `database/seed.sql` using a MySQL account with database creation permission. Configure connection settings with environment variables:

```powershell
$env:LIBRIX_DB_URL = 'jdbc:mysql://localhost:3306/librix?serverTimezone=UTC'
$env:LIBRIX_DB_USER = 'librix_app'
$env:LIBRIX_DB_PASSWORD = 'replace-me'
```

No database password is stored in source code. The current demo shell falls back to local demo authentication when these variables are not configured.
