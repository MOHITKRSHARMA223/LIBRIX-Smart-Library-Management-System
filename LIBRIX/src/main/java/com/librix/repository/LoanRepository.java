package com.librix.repository;

import com.librix.model.Loan;
import com.librix.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByStatus(LoanStatus status);
    List<Loan> findTop8ByOrderByBorrowedAtDesc();
    List<Loan> findByMemberIdOrderByBorrowedAtDesc(Long memberId);
    long countByMemberIdAndStatus(Long memberId, LoanStatus status);
    List<Loan> findByStatusAndDueAtBeforeOrderByDueAtAsc(LoanStatus status, LocalDate date);
    long countByStatusAndDueAtBefore(LoanStatus status, LocalDate date);
}
