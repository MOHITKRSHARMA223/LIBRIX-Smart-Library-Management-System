package com.librix.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BorrowingRulesTest {
    @Test void calculatesNoFineBeforeDueDate() {
        assertEquals(new BigDecimal("0.00"), BorrowingRules.fine(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 9)));
    }
    @Test void calculatesDailyFineAfterDueDate() {
        assertEquals(new BigDecimal("6.00"), BorrowingRules.fine(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 13)));
    }
    @Test void rejectsInactiveMember() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateBorrow(false, 0, true));
    }
    @Test void rejectsMaximumActiveLoans() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateBorrow(true, 3, true));
    }
    @Test void rejectsUnavailableBook() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateBorrow(true, 0, false));
    }
    @Test void rejectsRenewalForOverdueLoan() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateRenewal(true, true, 0, false));
    }
    @Test void rejectsRenewalWhenBookHasWaitlist() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateRenewal(true, false, 0, true));
    }
    @Test void rejectsSecondRenewal() {
        assertThrows(IllegalStateException.class, () -> BorrowingRules.validateRenewal(true, false, 1, false));
    }
}
