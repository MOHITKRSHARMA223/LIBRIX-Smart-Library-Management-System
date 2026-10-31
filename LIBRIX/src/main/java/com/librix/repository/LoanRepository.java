package com.librix.repository;

import com.librix.model.Loan;
import com.librix.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByStatus(LoanStatus status);
    List<Loan> findTop8ByOrderByBorrowedAtDesc();
    long countByMemberIdAndStatus(Long memberId, LoanStatus status);
}
