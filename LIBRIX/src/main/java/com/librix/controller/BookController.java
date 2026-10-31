package com.librix.controller;

import com.librix.model.LibraryBook;
import com.librix.repository.LibraryBookRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final LibraryBookRepository books;
    public BookController(LibraryBookRepository books) { this.books = books; }
    @GetMapping public String list(@RequestParam(defaultValue = "") String query, Model model) { model.addAttribute("books", query.isBlank() ? books.findAll() : books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)); model.addAttribute("query", query); return "books/list"; }
    @GetMapping("/new") public String form(Model model) { model.addAttribute("book", new LibraryBook()); return "books/form"; }
    @PostMapping public String save(@Valid @ModelAttribute("book") LibraryBook book, BindingResult result) { if (result.hasErrors()) return "books/form"; books.save(book); return "redirect:/books"; }
    @PostMapping("/{id}/delete") public String delete(@PathVariable Long id) { books.deleteById(id); return "redirect:/books"; }
}
