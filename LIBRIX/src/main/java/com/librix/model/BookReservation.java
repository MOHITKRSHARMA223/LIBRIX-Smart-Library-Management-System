package com.librix.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_reservations", indexes = {
    @Index(name = "idx_reservations_status", columnList = "status"),
    @Index(name = "idx_reservations_member", columnList = "member_id")
})
public class BookReservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LibraryBook book;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LibraryMember member;
    @Column(nullable = false)
    private LocalDate requestedAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.REQUESTED;

    protected BookReservation() { }

    public BookReservation(LibraryBook book, LibraryMember member) {
        this.book = book;
        this.member = member;
        this.requestedAt = LocalDate.now();
    }

    public Long getId() { return id; }
    public LibraryBook getBook() { return book; }
    public LibraryMember getMember() { return member; }
    public LocalDate getRequestedAt() { return requestedAt; }
    public ReservationStatus getStatus() { return status; }
    public void markReady() { status = ReservationStatus.READY; }
    public void cancel() { status = ReservationStatus.CANCELLED; }
    public void fulfill() { status = ReservationStatus.FULFILLED; }
}
