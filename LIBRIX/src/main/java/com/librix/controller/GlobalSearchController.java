package com.librix.controller;

import com.librix.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GlobalSearchController {
    private final LibraryBookRepository books; private final LibraryMemberRepository members; private final CategoryRepository categories; private final AuthorRepository authors; private final PublisherRepository publishers;
    public GlobalSearchController(LibraryBookRepository books, LibraryMemberRepository members, CategoryRepository categories, AuthorRepository authors, PublisherRepository publishers) { this.books = books; this.members = members; this.categories = categories; this.authors = authors; this.publishers = publishers; }
    @GetMapping("/search") public String search(@RequestParam(defaultValue = "") String query, Model model) { model.addAttribute("query", query); if (!query.isBlank()) { model.addAttribute("books", books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)); model.addAttribute("members", members.findByEmailIgnoreCase(query).stream().toList()); model.addAttribute("categories", categories.findByNameContainingIgnoreCase(query)); model.addAttribute("authors", authors.findByNameContainingIgnoreCase(query)); model.addAttribute("publishers", publishers.findByNameContainingIgnoreCase(query)); } else { model.addAttribute("books", java.util.List.of()); model.addAttribute("members", java.util.List.of()); model.addAttribute("categories", java.util.List.of()); model.addAttribute("authors", java.util.List.of()); model.addAttribute("publishers", java.util.List.of()); } return "search/index"; }
}
