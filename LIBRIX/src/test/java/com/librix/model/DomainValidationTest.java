package com.librix.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainValidationTest {
    @Test void bookRejectsAvailableCopiesAboveTotal() {
        assertThrows(IllegalArgumentException.class, () -> new Book(1, "", "Title", "Author", "Category", 1, 2));
    }
    @Test void memberRejectsMalformedEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Member(1, "Asha", "invalid", true));
    }
    @Test void studentCannotManageLibrary() {
        assertFalse(new UserSession(1, "student", Role.STUDENT, java.time.Instant.now()).canManageLibrary());
    }
}
