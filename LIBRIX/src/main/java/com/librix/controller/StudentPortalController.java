package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.model.ReservationStatus;
import com.librix.repository.*;
import com.librix.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentPortalController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans; private final BookReservationRepository reservations; private final ReservationService reservationService;
    public StudentPortalController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans, BookReservationRepository reservations, ReservationService reservationService) { this.books = books; this.members = members; this.loans = loans; this.reservations = reservations; this.reservationService = reservationService; }
    @GetMapping("/dashboard") public String dashboard(Authentication authentication, Model model) { var member = members.findByUser_Username(authentication.getName()); var history = member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(java.util.List::of); var studentReservations = member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId())).orElseGet(java.util.List::of); model.addAttribute("studentName", authentication.getName()); model.addAttribute("borrowedCount", history.stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE).count()); model.addAttribute("readCount", history.stream().filter(l -> l.getStatus() == LoanStatus.RETURNED).count()); model.addAttribute("overdueCount", history.stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE && l.getDueAt().isBefore(java.time.LocalDate.now())).count()); model.addAttribute("reservationCount", studentReservations.stream().filter(r -> r.getStatus() == ReservationStatus.REQUESTED || r.getStatus() == ReservationStatus.READY).count()); model.addAttribute("history", history); model.addAttribute("popularBooks", books.findAll().stream().limit(6).toList()); return "student/dashboard"; }
    @GetMapping("/catalog") public String catalog(@RequestParam(defaultValue = "") String query, Authentication authentication, Model model) { var results = query.isBlank() ? books.findAll() : books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query); var member = members.findByUser_Username(authentication.getName()); var reservedBookIds = member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId()).stream().filter(r -> r.getStatus() == ReservationStatus.REQUESTED || r.getStatus() == ReservationStatus.READY).map(r -> r.getBook().getId()).collect(java.util.stream.Collectors.toSet())).orElseGet(java.util.Set::of); model.addAttribute("books", results); model.addAttribute("reservedBookIds", reservedBookIds); model.addAttribute("query", query); return "student/catalog"; }
    @GetMapping("/my-books") public String myBooks(Authentication authentication, Model model) { var member = members.findByUser_Username(authentication.getName()); model.addAttribute("loans", member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(java.util.List::of)); model.addAttribute("reservations", member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId())).orElseGet(java.util.List::of)); return "student/my-books"; }
    @PostMapping("/catalog/{bookId}/reserve") public String reserve(@PathVariable Long bookId, Authentication authentication) { reservationService.reserve(bookId, authentication.getName()); return "redirect:/student/my-books"; }
    @PostMapping("/reservations/{id}/cancel") public String cancelReservation(@PathVariable Long id, Authentication authentication) { reservationService.cancel(id, authentication.getName()); return "redirect:/student/my-books"; }
}
