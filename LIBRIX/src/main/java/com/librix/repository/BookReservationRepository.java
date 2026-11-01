package com.librix.repository;

import com.librix.model.BookReservation;
import com.librix.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    List<BookReservation> findByMemberIdOrderByRequestedAtDesc(Long memberId);
    List<BookReservation> findTop100ByOrderByRequestedAtDesc();
    boolean existsByBookIdAndMemberIdAndStatusIn(Long bookId, Long memberId, List<ReservationStatus> statuses);
    long countByStatusIn(List<ReservationStatus> statuses);
}
