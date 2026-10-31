package com.librix.service;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class LibraryService {
    private final LibraryBookRepository books;
    private final LibraryMemberRepository members;
    private final LoanRepository loans;
    public LibraryService(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans) { this.books = books; this.members = members; this.loans = loans; }

    @Transactional
    public Loan borrow(Long bookId, Long memberId) {
        LibraryBook book = books.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        LibraryMember member = members.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        long activeLoans = loans.countByMemberIdAndStatus(memberId, LoanStatus.ACTIVE);
        BorrowingRules.validateBorrow(member.isActive(), (int) activeLoans, book.isAvailable());
        book.borrowCopy();
        books.save(book);
        return loans.save(new Loan(book, member, LocalDate.now(), LocalDate.now().plusDays(BorrowingRules.BORROWING_DAYS)));
    }

    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = loans.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        if (loan.getStatus() != LoanStatus.ACTIVE) throw new IllegalStateException("Loan is already closed");
        loan.getBook().returnCopy();
        loan.returnLoan(LocalDate.now());
        books.save(loan.getBook());
        loans.save(loan);
    }
}
