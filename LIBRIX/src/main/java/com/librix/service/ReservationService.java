package com.librix.service;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReservationService {
    private final LibraryBookRepository books;
    private final LibraryMemberRepository members;
    private final BookReservationRepository reservations;

    public ReservationService(LibraryBookRepository books, LibraryMemberRepository members, BookReservationRepository reservations) {
        this.books = books;
        this.members = members;
        this.reservations = reservations;
    }

    @Transactional
    public BookReservation reserve(Long bookId, String username) {
        LibraryBook book = books.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        LibraryMember member = members.findByUser_Username(username).orElseThrow(() -> new IllegalArgumentException("Student profile not found"));
        if (book.isAvailable()) throw new IllegalStateException("This book is available to borrow now");
        if (reservations.existsByBookIdAndMemberIdAndStatusIn(bookId, member.getId(), List.of(ReservationStatus.REQUESTED, ReservationStatus.READY))) {
            throw new IllegalStateException("You already have an active reservation for this book");
        }
        return reservations.save(new BookReservation(book, member));
    }

    @Transactional
    public void cancel(Long reservationId, String username) {
        BookReservation reservation = reservations.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (!reservation.getMember().getUser().getUsername().equalsIgnoreCase(username)) {
            throw new IllegalStateException("You can only manage your own reservations");
        }
        if (reservation.getStatus() == ReservationStatus.FULFILLED) throw new IllegalStateException("Fulfilled reservations cannot be cancelled");
        reservation.cancel();
        reservations.save(reservation);
    }

    @Transactional
    public void markReady(Long reservationId) {
        BookReservation reservation = reservations.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() != ReservationStatus.REQUESTED) throw new IllegalStateException("Only requested reservations can be marked ready");
        reservation.markReady();
        reservations.save(reservation);
    }

    @Transactional
    public void cancelByAdmin(Long reservationId) {
        BookReservation reservation = reservations.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.cancel();
        reservations.save(reservation);
    }
}
