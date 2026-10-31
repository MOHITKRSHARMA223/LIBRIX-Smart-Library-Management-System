package com.librix.controller;

import com.librix.model.LoanStatus;
import com.librix.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentPortalController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final LoanRepository loans;
    public StudentPortalController(LibraryBookRepository books, LibraryMemberRepository members, LoanRepository loans) { this.books = books; this.members = members; this.loans = loans; }
    @GetMapping("/dashboard") public String dashboard(Authentication authentication, Model model) { var member = members.findByUser_Username(authentication.getName()); var history = member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(java.util.List::of); model.addAttribute("studentName", authentication.getName()); model.addAttribute("borrowedCount", history.stream().filter(l -> l.getStatus() == LoanStatus.ACTIVE).count()); model.addAttribute("readCount", history.stream().filter(l -> l.getStatus() == LoanStatus.RETURNED).count()); model.addAttribute("history", history); model.addAttribute("popularBooks", books.findAll().stream().limit(6).toList()); return "student/dashboard"; }
    @GetMapping("/catalog") public String catalog(@RequestParam(defaultValue = "") String query, Model model) { model.addAttribute("books", query.isBlank() ? books.findAll() : books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)); model.addAttribute("query", query); return "student/catalog"; }
    @GetMapping("/my-books") public String myBooks(Authentication authentication, Model model) { var member = members.findByUser_Username(authentication.getName()); model.addAttribute("loans", member.map(m -> loans.findByMemberIdOrderByBorrowedAtDesc(m.getId())).orElseGet(java.util.List::of)); return "student/my-books"; }
}
