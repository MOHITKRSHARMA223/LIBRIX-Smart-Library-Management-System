package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.model.ReservationStatus;
import com.librix.repository.*;
import com.librix.service.LibraryService;
import com.librix.service.ReservationService;
import com.librix.model.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentPortalController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans; private final BookReservationRepository reservations; private final ReservationService reservationService;
    private final WishlistRepository wishlist; private final ReadingProgressRepository progress; private final StudyResourceRepository resources; private final UserNotificationRepository notifications; private final AppUserRepository users; private final ActivityLogRepository activityLogs;
    private final StudyPlanItemRepository studyPlan; private final SeatBookingRepository seatBookings;
    private final GamificationProfileRepository gamification;
    private final LibraryService libraryService;
    public StudentPortalController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans, BookReservationRepository reservations, ReservationService reservationService,
                                   WishlistRepository wishlist, ReadingProgressRepository progress, StudyResourceRepository resources, UserNotificationRepository notifications,
                                   AppUserRepository users, ActivityLogRepository activityLogs, StudyPlanItemRepository studyPlan, SeatBookingRepository seatBookings, LibraryService libraryService, GamificationProfileRepository gamification) {
        this.books = books; this.members = members; this.loans = loans; this.reservations = reservations; this.reservationService = reservationService;
        this.wishlist = wishlist; this.progress = progress; this.resources = resources; this.notifications = notifications;         this.users = users; this.activityLogs = activityLogs; this.studyPlan = studyPlan; this.seatBookings = seatBookings; this.libraryService = libraryService; this.gamification = gamification;
    }
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        var member = members.findByUser_Username(username);
        var history = member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(List::of);
        var studentReservations = member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId())).orElseGet(List::of);
        Set<String> readCategories = history.stream().map(loan -> loan.getBook().getCategory()).filter(java.util.Objects::nonNull).collect(Collectors.toSet());
        List<LibraryBook> recommended = books.findAll().stream()
                .filter(book -> readCategories.isEmpty() || readCategories.contains(book.getCategory()))
                .limit(6).toList();
        LocalDate today = LocalDate.now();
        model.addAttribute("studentName", username);
        model.addAttribute("borrowedCount", history.stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE).count());
        model.addAttribute("readCount", history.stream().filter(l -> l.getStatus() == LoanStatus.RETURNED).count());
        model.addAttribute("overdueCount", history.stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE && l.getDueAt().isBefore(today)).count());
        model.addAttribute("reservationCount", studentReservations.stream().filter(r -> r.getStatus() == ReservationStatus.REQUESTED || r.getStatus() == ReservationStatus.READY).count());
        model.addAttribute("today", today);
        model.addAttribute("attentionLoans", history.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.ACTIVE && !loan.getDueAt().isAfter(today.plusDays(3)))
                .sorted(Comparator.comparing((Loan loan) -> loan.getDueAt()))
                .limit(5)
                .toList());
        model.addAttribute("history", history);
        model.addAttribute("recommendedBooks", recommended.isEmpty() ? books.findAll().stream().limit(6).toList() : recommended);
        model.addAttribute("progress", progress.findForUser(username).stream().limit(4).toList());
        model.addAttribute("unreadNotifications", notifications.countUnreadForUser(username));
        var plan = studyPlan.findForUser(username);
        model.addAttribute("studyPlan", plan.stream().limit(3).toList());
        model.addAttribute("todayPlanCount", plan.stream().filter(item -> item.getScheduledDate().equals(LocalDate.now()) && !item.isCompleted()).count());
        model.addAttribute("activeLoanLoad", history.stream().filter(loan -> loan.getStatus() == LoanStatus.ACTIVE).count());
        model.addAttribute("averageProgress", progress.findForUser(username).stream().mapToInt(item -> item.getPercentComplete()).average().orElse(0));
        model.addAttribute("completedProgress", progress.findForUser(username).stream().filter(item -> item.getPercentComplete() == 100).count());
        var profile = gamification.findForUser(username).orElse(null);
        model.addAttribute("xp", profile == null ? 0 : profile.getXp());
        model.addAttribute("badge", profile == null ? "New explorer" : profile.getBadge());
        model.addAttribute("completedPlans", plan.stream().filter(item -> item.isCompleted()).count());
        model.addAttribute("plannedMinutes", plan.stream().filter(item -> !item.isCompleted()).mapToInt(item -> item.getDurationMinutes()).sum());
        model.addAttribute("nextSeatBooking", seatBookings.findForUser(username).stream()
                .filter(booking -> booking.getStatus() == SeatBookingStatus.CONFIRMED && !booking.getBookingDate().isBefore(LocalDate.now())).findFirst().orElse(null));
        return "student/dashboard";
    }
    @GetMapping("/insights")
    public String insights(Authentication authentication, Model model) {
        String username = authentication.getName();
        var plan = studyPlan.findForUser(username);
        var reading = progress.findForUser(username);
        model.addAttribute("averageProgress", reading.stream().mapToInt(item -> item.getPercentComplete()).average().orElse(0));
        model.addAttribute("completedBooks", reading.stream().filter(item -> item.getPercentComplete() == 100).count());
        model.addAttribute("completedPlans", plan.stream().filter(StudyPlanItem::isCompleted).count());
        model.addAttribute("plannedMinutes", plan.stream().filter(item -> !item.isCompleted()).mapToInt(StudyPlanItem::getDurationMinutes).sum());
        var profile = gamification.findForUser(username).orElse(null);
        model.addAttribute("xp", profile == null ? 0 : profile.getXp());
        model.addAttribute("badge", profile == null ? "New explorer" : profile.getBadge());
        return "student/insights";
    }
    @GetMapping("/catalog") public String catalog(@RequestParam(defaultValue = "") String query, Authentication authentication, Model model) {
        var results = query.isBlank() ? books.findAll() : books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
        var member = members.findByUser_Username(authentication.getName());
        var reservedBookIds = member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId()).stream().filter(r -> r.getStatus() == ReservationStatus.REQUESTED || r.getStatus() == ReservationStatus.READY).map(r -> r.getBook().getId()).collect(Collectors.toSet())).orElseGet(Set::of);
        var wishlistBookIds = wishlist.findForUser(authentication.getName()).stream().map(item -> item.getBook().getId()).collect(Collectors.toSet());
        model.addAttribute("books", results); model.addAttribute("reservedBookIds", reservedBookIds); model.addAttribute("wishlistBookIds", wishlistBookIds); model.addAttribute("query", query); return "student/catalog";
    }
    @GetMapping("/my-books") public String myBooks(Authentication authentication, Model model) {
        var member = members.findByUser_Username(authentication.getName());
        model.addAttribute("loans", member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(List::of));
        model.addAttribute("reservations", member.map(m -> reservations.findByMemberIdOrderByRequestedAtDesc(m.getId())).orElseGet(List::of));
        model.addAttribute("wishlist", wishlist.findForUser(authentication.getName()));
        model.addAttribute("progress", progress.findForUser(authentication.getName()));
        return "student/my-books";
    }
    @PostMapping("/catalog/{bookId}/reserve") public String reserve(@PathVariable Long bookId, Authentication authentication) { reservationService.reserve(bookId, authentication.getName()); return "redirect:/student/my-books"; }
    @PostMapping("/reservations/{id}/cancel") public String cancelReservation(@PathVariable Long id, Authentication authentication) { reservationService.cancel(id, authentication.getName()); return "redirect:/student/my-books"; }
    @PostMapping("/loans/{id}/renew") public String renewLoan(@PathVariable Long id, Authentication authentication) { libraryService.renewLoan(id, authentication.getName()); return "redirect:/student/my-books"; }
    @PostMapping("/catalog/{bookId}/wishlist")
    public String wishlist(@PathVariable Long bookId, Authentication authentication) {
        wishlist.findForUserAndBook(authentication.getName(), bookId).orElseGet(() -> {
            AppUser user = users.findByUsernameIgnoreCase(authentication.getName()).orElseThrow();
            LibraryBook book = books.findById(bookId).orElseThrow();
            activityLogs.save(new ActivityLog(user, "WISHLIST_ADDED", "STUDENT_PORTAL", "Added " + book.getTitle() + " to wishlist."));
            return wishlist.save(new WishlistItem(user, book));
        });
        return "redirect:/student/catalog";
    }
    @PostMapping("/wishlist/{id}/remove") public String removeWishlist(@PathVariable Long id) { wishlist.deleteById(id); return "redirect:/student/my-books"; }
    @PostMapping("/progress/{bookId}")
    public String updateProgress(@PathVariable Long bookId, @RequestParam int percent, Authentication authentication) {
        int bounded = Math.max(0, Math.min(100, percent));
        ReadingProgress item = progress.findForUserAndBook(authentication.getName(), bookId).orElseGet(() -> new ReadingProgress(
                users.findByUsernameIgnoreCase(authentication.getName()).orElseThrow(), books.findById(bookId).orElseThrow(), bounded));
        item.updatePercent(bounded); progress.save(item);
        return "redirect:/student/my-books";
    }
    @GetMapping("/resources") public String resources(Model model) { model.addAttribute("resources", resources.findAll()); return "student/resources"; }
    @GetMapping("/planner") public String planner(Authentication authentication, Model model) {
        model.addAttribute("plan", studyPlan.findForUser(authentication.getName()));
        return "student/planner";
    }
    @PostMapping("/planner")
    public String addPlan(@RequestParam String title, @RequestParam LocalDate scheduledDate, @RequestParam int durationMinutes, Authentication authentication) {
        if (title.isBlank() || scheduledDate.isBefore(LocalDate.now()) || durationMinutes < 15 || durationMinutes > 480) return "redirect:/student/planner?invalid";
        AppUser user = users.findByUsernameIgnoreCase(authentication.getName()).orElseThrow();
        studyPlan.save(new StudyPlanItem(user, title.trim(), scheduledDate, durationMinutes));
        activityLogs.save(new ActivityLog(user, "STUDY_PLAN_CREATED", "STUDENT_PORTAL", "Created study goal: " + title.trim()));
        return "redirect:/student/planner";
    }
    @PostMapping("/planner/{id}/toggle")
    public String togglePlan(@PathVariable Long id, Authentication authentication) {
        StudyPlanItem item = studyPlan.findById(id).orElseThrow();
        if (!item.getUser().getUsername().equalsIgnoreCase(authentication.getName())) throw new org.springframework.security.access.AccessDeniedException("Study plan item belongs to another student.");
        if (item.isCompleted()) item.markOpen(); else item.markCompleted();
        studyPlan.save(item);
        return "redirect:/student/planner";
    }
    @PostMapping("/planner/{id}/delete")
    public String deletePlan(@PathVariable Long id, Authentication authentication) {
        StudyPlanItem item = studyPlan.findById(id).orElseThrow();
        if (!item.getUser().getUsername().equalsIgnoreCase(authentication.getName())) throw new org.springframework.security.access.AccessDeniedException("Study plan item belongs to another student.");
        studyPlan.delete(item);
        return "redirect:/student/planner";
    }
    @GetMapping("/seats") public String seats(Authentication authentication, Model model) {
        model.addAttribute("bookings", seatBookings.findForUser(authentication.getName()));
        model.addAttribute("seats", List.of("A-01", "A-02", "A-03", "B-01", "B-02", "Focus-01"));
        return "student/seats";
    }
    @PostMapping("/seats")
    public String bookSeat(@RequestParam String seatLabel, @RequestParam LocalDate bookingDate, @RequestParam LocalTime startTime,
                           @RequestParam LocalTime endTime, Authentication authentication) {
        if (bookingDate.isBefore(LocalDate.now()) || !endTime.isAfter(startTime) || startTime.isBefore(LocalTime.of(7, 0)) || endTime.isAfter(LocalTime.of(22, 0)))
            return "redirect:/student/seats?invalid";
        if (seatBookings.existsOverlapping(seatLabel, bookingDate, startTime, endTime, SeatBookingStatus.CONFIRMED))
            return "redirect:/student/seats?occupied";
        AppUser user = users.findByUsernameIgnoreCase(authentication.getName()).orElseThrow();
        seatBookings.save(new SeatBooking(user, seatLabel, bookingDate, startTime, endTime));
        activityLogs.save(new ActivityLog(user, "SEAT_BOOKED", "STUDENT_PORTAL", "Reserved " + seatLabel + " for " + bookingDate + "."));
        return "redirect:/student/seats";
    }
    @PostMapping("/seats/{id}/cancel")
    public String cancelSeat(@PathVariable Long id, Authentication authentication) {
        SeatBooking booking = seatBookings.findById(id).orElseThrow();
        if (!booking.getUser().getUsername().equalsIgnoreCase(authentication.getName())) throw new org.springframework.security.access.AccessDeniedException("Seat booking belongs to another student.");
        booking.cancel(); seatBookings.save(booking);
        return "redirect:/student/seats";
    }
    @GetMapping("/notifications") public String notifications(Authentication authentication, Model model) { model.addAttribute("notifications", notifications.findForUser(authentication.getName())); return "student/notifications"; }
    @PostMapping("/notifications/{id}/read") public String markNotificationRead(@PathVariable Long id, Authentication authentication) {
        UserNotification item = notifications.findById(id).orElseThrow();
        if (!item.getUser().getUsername().equalsIgnoreCase(authentication.getName())) throw new IllegalStateException("You can only manage your own notifications");
        item.markRead(); notifications.save(item); return "redirect:/student/notifications";
    }
}
