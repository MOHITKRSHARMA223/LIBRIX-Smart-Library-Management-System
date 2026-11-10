package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CommunityController {
    private final AppUserRepository users;
    private final LibraryBookRepository books;
    private final BookReviewRepository reviews;
    private final ExchangeListingRepository exchanges;
    private final ResearchPaperRepository papers;
    private final GamificationProfileRepository profiles;
    private final SeatBookingRepository seats;

    public CommunityController(AppUserRepository users, LibraryBookRepository books, BookReviewRepository reviews,
                               ExchangeListingRepository exchanges, ResearchPaperRepository papers,
                               GamificationProfileRepository profiles, SeatBookingRepository seats) {
        this.users = users; this.books = books; this.reviews = reviews; this.exchanges = exchanges;
        this.papers = papers; this.profiles = profiles; this.seats = seats;
    }

    @GetMapping("/student/community")
    @PreAuthorize("hasRole('STUDENT')")
    public String community(Model model) {
        model.addAttribute("reviews", reviews.findTop100ByOrderByCreatedAtDesc());
        model.addAttribute("books", books.findAll());
        return "student/community";
    }

    @PostMapping("/student/community/review")
    @PreAuthorize("hasRole('STUDENT')")
    public String review(@RequestParam Long bookId, @RequestParam int rating, @RequestParam String comment,
                         Authentication authentication) {
        if (rating < 1 || rating > 5 || comment == null || comment.isBlank()) return "redirect:/student/community?invalid";
        AppUser user = currentUser(authentication);
        LibraryBook book = books.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        BookReview review = reviews.findForUserAndBook(authentication.getName(), bookId)
                .orElse(new BookReview(user, book, rating, comment.trim()));
        if (review.getId() != null) review.update(rating, comment.trim());
        reviews.save(review);
        GamificationProfile profile = profiles.findForUser(authentication.getName())
                .orElseGet(() -> new GamificationProfile(user));
        profile.award(10); profiles.save(profile);
        return "redirect:/student/community";
    }

    @GetMapping("/student/exchange")
    @PreAuthorize("hasRole('STUDENT')")
    public String exchange(Model model) {
        model.addAttribute("listings", exchanges.findByStatusOrderByCreatedAtDesc(ExchangeStatus.OPEN));
        return "student/exchange";
    }

    @PostMapping("/student/exchange")
    @PreAuthorize("hasRole('STUDENT')")
    public String addExchange(@RequestParam String title, @RequestParam String description, Authentication authentication) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) return "redirect:/student/exchange?invalid";
        exchanges.save(new ExchangeListing(currentUser(authentication), title.trim(), description.trim()));
        return "redirect:/student/exchange";
    }

    @PostMapping("/student/exchange/{id}/close")
    @PreAuthorize("hasRole('STUDENT')")
    public String closeExchange(@PathVariable Long id, Authentication authentication) {
        ExchangeListing listing = exchanges.findById(id).orElseThrow();
        if (!listing.getUser().getUsername().equalsIgnoreCase(authentication.getName())) throw new AccessDeniedException("You can only close your own listing.");
        listing.close(); exchanges.save(listing);
        return "redirect:/student/exchange";
    }

    @GetMapping("/student/research")
    @PreAuthorize("hasRole('STUDENT')")
    public String research(Model model) { model.addAttribute("papers", papers.findTop100ByOrderByCreatedAtDesc()); return "student/research"; }

    @PostMapping("/student/research")
    @PreAuthorize("hasRole('STUDENT')")
    public String submitResearch(@RequestParam String title, @RequestParam String authors, @RequestParam String subject,
                                 @RequestParam String abstractText, Authentication authentication) {
        if (isBlank(title) || isBlank(authors) || isBlank(subject) || isBlank(abstractText)) return "redirect:/student/research?invalid";
        papers.save(new ResearchPaper(currentUser(authentication), title.trim(), authors.trim(), subject.trim(), abstractText.trim()));
        return "redirect:/student/research";
    }

    @GetMapping("/student/assistant")
    @PreAuthorize("hasRole('STUDENT')")
    public String assistant(@RequestParam(defaultValue = "") String query, Model model) {
        model.addAttribute("query", query);
        model.addAttribute("results", query.isBlank() ? List.of() : books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query));
        model.addAttribute("answer", assistantAnswer(query));
        return "student/assistant";
    }

    @GetMapping("/student/occupancy")
    @PreAuthorize("hasRole('STUDENT')")
    public String occupancy(Model model) {
        long occupied = seats.findTop100ByOrderByBookingDateDescStartTimeDesc().stream()
                .filter(booking -> booking.getBookingDate().equals(LocalDate.now()) && booking.getStatus() == SeatBookingStatus.CONFIRMED).count();
        model.addAttribute("occupied", occupied); model.addAttribute("capacity", 6); model.addAttribute("available", Math.max(0, 6 - occupied));
        return "student/occupancy";
    }

    @GetMapping("/admin/community")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
    public String adminCommunity(Model model) {
        model.addAttribute("reviews", reviews.findTop100ByOrderByCreatedAtDesc());
        model.addAttribute("listings", exchanges.findByStatusOrderByCreatedAtDesc(ExchangeStatus.OPEN));
        model.addAttribute("papers", papers.findTop100ByOrderByCreatedAtDesc());
        return "admin/community";
    }

    @PostMapping("/admin/community/exchange/{id}/close")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
    public String adminCloseExchange(@PathVariable Long id) {
        ExchangeListing listing = exchanges.findById(id).orElseThrow();
        listing.close(); exchanges.save(listing);
        return "redirect:/admin/community";
    }

    private AppUser currentUser(Authentication authentication) {
        return users.findByUsernameIgnoreCase(authentication.getName()).orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
    private boolean isBlank(String value) { return value == null || value.isBlank(); }
    private String assistantAnswer(String query) {
        if (query == null || query.isBlank()) return "Search books by title or author, or ask about borrowing, renewals, reservations, and seats.";
        String normalized = query.toLowerCase();
        if (normalized.contains("renew")) return "Active, non-overdue loans may be renewed once when no waitlist is active.";
        if (normalized.contains("seat")) return "Seats are bookable from 07:00 to 22:00 and overlapping bookings are blocked.";
        if (normalized.contains("borrow")) return "Students may borrow up to three books for fourteen days.";
        return "Here are the matching books from the LIBRIX catalog.";
    }
}
