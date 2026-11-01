package com.librix.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "seat_bookings", indexes = {
        @Index(name = "idx_seat_booking_date", columnList = "booking_date"),
        @Index(name = "idx_seat_booking_user", columnList = "user_id")
})
public class SeatBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @Column(name = "seat_label", nullable = false, length = 30)
    private String seatLabel;
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private SeatBookingStatus status = SeatBookingStatus.CONFIRMED;

    protected SeatBooking() { }
    public SeatBooking(AppUser user, String seatLabel, LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        this.user = user; this.seatLabel = seatLabel; this.bookingDate = bookingDate; this.startTime = startTime; this.endTime = endTime;
    }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public String getSeatLabel() { return seatLabel; }
    public LocalDate getBookingDate() { return bookingDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public SeatBookingStatus getStatus() { return status; }
    public void cancel() { status = SeatBookingStatus.CANCELLED; }
}
