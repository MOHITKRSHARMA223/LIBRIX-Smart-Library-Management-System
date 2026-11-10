package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
public class AdminPortalController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans; private final BookReservationRepository reservations; private final AppUserRepository users; private final StudyPlanItemRepository studyPlan; private final SeatBookingRepository seatBookings;
    public AdminPortalController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans, BookReservationRepository reservations, AppUserRepository users, StudyPlanItemRepository studyPlan, SeatBookingRepository seatBookings) { this.books = books; this.members = members; this.loans = loans; this.reservations = reservations; this.users = users; this.studyPlan = studyPlan; this.seatBookings = seatBookings; }
    @GetMapping({"/admin", "/admin/dashboard"})
    public String dashboard(Model model) {
        var inventory = books.findAll();
        int totalCopies = inventory.stream().mapToInt(LibraryBook::getTotalCopies).sum();
        int availableCopies = inventory.stream().mapToInt(LibraryBook::getAvailableCopies).sum();
        model.addAttribute("totalBooks", books.count());
        model.addAttribute("availableCopies", availableCopies);
        model.addAttribute("inventoryUtilization", totalCopies == 0 ? 0 : (100 * (totalCopies - availableCopies) / totalCopies));
        model.addAttribute("activeMembers", members.countByActiveTrue());
        model.addAttribute("activeLoans", loans.countByStatus(LoanStatus.ACTIVE));
        model.addAttribute("overdueLoans", loans.countByStatusAndDueAtBefore(LoanStatus.ACTIVE, LocalDate.now()));
        model.addAttribute("dueSoonLoans", loans.countByStatusAndDueAtBetween(LoanStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusDays(3)));
        model.addAttribute("lowStockBooks", books.findAll().stream().filter(book -> book.getAvailableCopies() <= 1).count());
        model.addAttribute("circulationLoad", loans.countByStatus(LoanStatus.ACTIVE) + reservations.countByStatusIn(java.util.List.of(ReservationStatus.REQUESTED, ReservationStatus.READY)));
        model.addAttribute("pendingReservations", reservations.countByStatusIn(java.util.List.of(com.librix.model.ReservationStatus.REQUESTED, com.librix.model.ReservationStatus.READY)));
        model.addAttribute("pendingUsers", users.findAll().stream().filter(user -> user.getRole() == Role.STUDENT && !user.isActive()).count());
        model.addAttribute("openStudyPlans", studyPlan.count());
        model.addAttribute("seatBookings", seatBookings.count());
        model.addAttribute("todaySeats", seatBookings.findTop100ByOrderByBookingDateDescStartTimeDesc().stream()
                .filter(booking -> booking.getBookingDate().equals(LocalDate.now()) && booking.getStatus() == SeatBookingStatus.CONFIRMED).count());
        model.addAttribute("pendingWork", users.findAll().stream().filter(user -> user.getRole() == Role.STUDENT && !user.isActive()).count()
                + reservations.countByStatusIn(java.util.List.of(ReservationStatus.REQUESTED, ReservationStatus.READY)));
        model.addAttribute("recentLoans", loans.findTop8ByOrderByBorrowedAtDesc());
        return "admin/dashboard";
    }
    @GetMapping("/admin/insights")
    public String insights(Model model) {
        var inventory = books.findAll();
        int totalCopies = inventory.stream().mapToInt(LibraryBook::getTotalCopies).sum();
        int availableCopies = inventory.stream().mapToInt(LibraryBook::getAvailableCopies).sum();
        model.addAttribute("inventoryUtilization", totalCopies == 0 ? 0 : (100 * (totalCopies - availableCopies) / totalCopies));
        model.addAttribute("todaySeats", seatBookings.findTop100ByOrderByBookingDateDescStartTimeDesc().stream()
                .filter(booking -> booking.getBookingDate().equals(LocalDate.now()) && booking.getStatus() == SeatBookingStatus.CONFIRMED).count());
        model.addAttribute("dueSoonLoans", loans.countByStatusAndDueAtBetween(LoanStatus.ACTIVE, LocalDate.now(), LocalDate.now().plusDays(3)));
        model.addAttribute("lowStockBooks", inventory.stream().filter(book -> book.getAvailableCopies() <= 1).count());
        return "admin/insights";
    }
    @GetMapping("/admin/analytics")
    public String analytics(Model model) {
        model.addAttribute("totalBooks", books.count());
        model.addAttribute("activeLoans", loans.countByStatus(LoanStatus.ACTIVE));
        model.addAttribute("activeMembers", members.countByActiveTrue());
        model.addAttribute("reservations", reservations.countByStatusIn(java.util.List.of(ReservationStatus.REQUESTED, ReservationStatus.READY)));
        model.addAttribute("seatBookings", seatBookings.count());
        model.addAttribute("studyPlans", studyPlan.count());
        return "admin/analytics";
    }
}
