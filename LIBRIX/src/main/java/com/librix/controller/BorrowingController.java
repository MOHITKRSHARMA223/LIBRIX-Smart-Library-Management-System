package com.librix.controller;

import com.librix.repository.*;
import com.librix.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/borrowing")
public class BorrowingController {
    private final LibraryService service; private final LoanRepository loans; private final LibraryBookRepository books; private final LibraryMemberRepository members;
    public BorrowingController(LibraryService service, LoanRepository loans, LibraryBookRepository books, LibraryMemberRepository members) { this.service = service; this.loans = loans; this.books = books; this.members = members; }
    @GetMapping public String list(Model model) { model.addAttribute("loans", loans.findAll()); model.addAttribute("books", books.findAll()); model.addAttribute("members", members.findAll()); return "borrowing/list"; }
    @PostMapping("/borrow") public String borrow(@RequestParam Long bookId, @RequestParam Long memberId) { service.borrow(bookId, memberId); return "redirect:/borrowing"; }
    @PostMapping("/{id}/return") public String returnLoan(@PathVariable Long id) { service.returnLoan(id); return "redirect:/borrowing"; }
}
