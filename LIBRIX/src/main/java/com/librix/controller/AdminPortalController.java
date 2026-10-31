package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
public class AdminPortalController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans;
    public AdminPortalController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans) { this.books = books; this.members = members; this.loans = loans; }
    @GetMapping({"/admin", "/admin/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", books.count());
        model.addAttribute("availableCopies", books.findAll().stream().mapToInt(b -> b.getAvailableCopies()).sum());
        model.addAttribute("activeMembers", members.countByActiveTrue());
        model.addAttribute("activeLoans", loans.countByStatus(LoanStatus.ACTIVE));
        model.addAttribute("recentLoans", loans.findTop8ByOrderByBorrowedAtDesc());
        return "admin/dashboard";
    }
}
