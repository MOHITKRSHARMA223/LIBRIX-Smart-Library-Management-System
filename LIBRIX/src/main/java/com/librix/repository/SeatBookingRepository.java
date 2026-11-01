package com.librix.repository;

import com.librix.model.SeatBooking;
import com.librix.model.SeatBookingStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    @Query("select booking from SeatBooking booking where lower(booking.user.username) = lower(:username) order by booking.bookingDate asc, booking.startTime asc")
    List<SeatBooking> findForUser(@Param("username") String username);
    List<SeatBooking> findTop100ByOrderByBookingDateDescStartTimeDesc();
    @Query("""
        select count(s) > 0 from SeatBooking s
        where s.seatLabel = :seat and s.bookingDate = :date and s.status = :status
          and s.startTime < :endTime and s.endTime > :startTime
        """)
    boolean existsOverlapping(@Param("seat") String seat, @Param("date") LocalDate date,
                              @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
                              @Param("status") SeatBookingStatus status);
}
