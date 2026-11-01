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
        model.addAttribute("totalBooks", books.count());
        model.addAttribute("availableCopies", books.findAll().stream().mapToInt(b -> b.getAvailableCopies()).sum());
        model.addAttribute("activeMembers", members.countByActiveTrue());
        model.addAttribute("activeLoans", loans.countByStatus(LoanStatus.ACTIVE));
        model.addAttribute("overdueLoans", loans.countByStatusAndDueAtBefore(LoanStatus.ACTIVE, LocalDate.now()));
        model.addAttribute("pendingReservations", reservations.countByStatusIn(java.util.List.of(com.librix.model.ReservationStatus.REQUESTED, com.librix.model.ReservationStatus.READY)));
        model.addAttribute("pendingUsers", users.findAll().stream().filter(user -> user.getRole() == Role.STUDENT && !user.isActive()).count());
        model.addAttribute("openStudyPlans", studyPlan.count());
        model.addAttribute("seatBookings", seatBookings.count());
        model.addAttribute("recentLoans", loans.findTop8ByOrderByBorrowedAtDesc());
        return "admin/dashboard";
    }
}
