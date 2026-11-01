package com.librix.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans", indexes = @Index(name = "idx_loans_status", columnList = "status"))
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private LibraryBook book;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private LibraryMember member;
    @Column(nullable = false)
    private LocalDate borrowedAt;
    @Column(nullable = false)
    private LocalDate dueAt;
    private LocalDate returnedAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private LoanStatus status = LoanStatus.ACTIVE;

    protected Loan() { }
    public Loan(LibraryBook book, LibraryMember member, LocalDate borrowedAt, LocalDate dueAt) { this.book = book; this.member = member; this.borrowedAt = borrowedAt; this.dueAt = dueAt; }
    public Long getId() { return id; }
    public LibraryBook getBook() { return book; }
    public LibraryMember getMember() { return member; }
    public LocalDate getBorrowedAt() { return borrowedAt; }
    public LocalDate getDueAt() { return dueAt; }
    public LocalDate getReturnedAt() { return returnedAt; }
    public LoanStatus getStatus() { return status; }
    public void returnLoan(LocalDate date) { returnedAt = date; status = LoanStatus.RETURNED; }
}
