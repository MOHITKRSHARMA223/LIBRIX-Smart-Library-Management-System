package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.repository.*;
import com.librix.service.BorrowingRules;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
public class AdminReportsController {
    private final LoanRepository loans;
    private final LibraryBookRepository books;
    private final LibraryMemberRepository members;

    public AdminReportsController(LoanRepository loans, LibraryBookRepository books, LibraryMemberRepository members) {
        this.loans = loans;
        this.books = books;
        this.members = members;
    }

    @GetMapping("/admin/reports")
    public String reports(Model model) {
        var overdue = loans.findByStatusAndDueAtBeforeOrderByDueAtAsc(LoanStatus.ACTIVE, LocalDate.now());
        model.addAttribute("overdueLoans", overdue);
        model.addAttribute("overdueFine", overdue.stream().mapToDouble(loan -> BorrowingRules.fine(loan.getDueAt(), LocalDate.now()).doubleValue()).sum());
        model.addAttribute("inventoryCount", books.count());
        model.addAttribute("memberCount", members.count());
        return "admin/reports";
    }

    @GetMapping(value = "/admin/reports/overdue.csv", produces = "text/csv")
    public void overdueCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=librix-overdue-loans.csv");
        response.setContentType("text/csv");
        var writer = response.getWriter();
        writer.println("book,member,email,due_date,days_overdue,estimated_fine");
        for (var loan : loans.findByStatusAndDueAtBeforeOrderByDueAtAsc(LoanStatus.ACTIVE, LocalDate.now())) {
            var days = java.time.temporal.ChronoUnit.DAYS.between(loan.getDueAt(), LocalDate.now());
            writer.printf("\"%s\",\"%s\",\"%s\",%s,%d,%s%n", csv(loan.getBook().getTitle()), csv(loan.getMember().getFullName()), csv(loan.getMember().getEmail()), loan.getDueAt(), days, BorrowingRules.fine(loan.getDueAt(), LocalDate.now()));
        }
    }

    private static String csv(String value) {
        return value == null ? "" : value.replace("\"", "\"\"");
    }
}
