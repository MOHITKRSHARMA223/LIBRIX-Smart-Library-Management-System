package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class DashboardController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans;
    public DashboardController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans) { this.books = books; this.members = members; this.loans = loans; }
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("totalBooks", books.count());
        model.addAttribute("availableCopies", books.findAll().stream().mapToInt(b -> b.getAvailableCopies()).sum());
        model.addAttribute("activeMembers", members.countByActiveTrue());
        model.addAttribute("activeLoans", loans.countByStatus(LoanStatus.ACTIVE));
        model.addAttribute("overdueLoans", loans.countByStatusAndDueAtBefore(LoanStatus.ACTIVE, today));
        model.addAttribute("dueSoonLoans", loans.countByStatusAndDueAtBetween(LoanStatus.ACTIVE, today, today.plusDays(3)));
        model.addAttribute("overdueLoanList", loans.findByStatusAndDueAtBeforeOrderByDueAtAsc(LoanStatus.ACTIVE, today));
        model.addAttribute("recentLoans", loans.findTop8ByOrderByBorrowedAtDesc());
        return "dashboard/index";
    }
}
