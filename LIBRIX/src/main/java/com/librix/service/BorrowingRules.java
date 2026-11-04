package com.librix.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class BorrowingRules {
    public static final int BORROWING_DAYS = 14;
    public static final int RENEWAL_DAYS = 7;
    public static final int MAX_RENEWALS = 1;
    public static final int MAX_ACTIVE_LOANS = 3;
    public static final BigDecimal DAILY_FINE = new BigDecimal("2.00");

    private BorrowingRules() { }

    public static void validateBorrow(boolean memberActive, int activeLoans, boolean bookAvailable) {
        if (!memberActive) throw new IllegalStateException("Inactive members cannot borrow");
        if (activeLoans >= MAX_ACTIVE_LOANS) throw new IllegalStateException("Maximum active books reached");
        if (!bookAvailable) throw new IllegalStateException("Book is unavailable");
    }

    public static void validateRenewal(boolean active, boolean overdue, int renewalCount, boolean hasWaitingReservations) {
        if (!active) throw new IllegalStateException("Only active loans can be renewed");
        if (overdue) throw new IllegalStateException("Overdue loans cannot be renewed");
        if (renewalCount >= MAX_RENEWALS) throw new IllegalStateException("This loan has already been renewed");
        if (hasWaitingReservations) throw new IllegalStateException("This book has an active waitlist");
    }

    public static BigDecimal fine(LocalDate dueDate, LocalDate endDate) {
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(dueDate, endDate));
        return DAILY_FINE.multiply(BigDecimal.valueOf(lateDays));
    }
}
