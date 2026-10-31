# Testing

The current automated tests cover fine calculation, borrowing limits, availability checks, role authorization, and model validation. Run them with:

```powershell
mvn test
```

MySQL integration tests are not claimed yet because they require an isolated configured database. Before release, add Testcontainers or a dedicated CI MySQL service for transaction, concurrent borrowing, DAO, reservation, and payment tests.
