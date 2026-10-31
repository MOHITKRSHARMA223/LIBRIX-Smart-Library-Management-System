package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.LinkedHashMap;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
public class CatalogController {
    private final CategoryRepository categories; private final AuthorRepository authors; private final PublisherRepository publishers; private final LibraryBookRepository books;
    public CatalogController(CategoryRepository categories, AuthorRepository authors, PublisherRepository publishers, LibraryBookRepository books) { this.categories = categories; this.authors = authors; this.publishers = publishers; this.books = books; }
    @GetMapping("/categories") public String categories(@RequestParam(defaultValue = "") String query, Model model) { var values = query.isBlank() ? categories.findAll() : categories.findByNameContainingIgnoreCase(query); var counts = new LinkedHashMap<Long, Long>(); values.forEach(c -> counts.put(c.getId(), books.countByCategoryIgnoreCase(c.getName()))); model.addAttribute("items", values); model.addAttribute("counts", counts); model.addAttribute("kind", "Categories"); model.addAttribute("query", query); return "catalog/list"; }
    @GetMapping("/authors") public String authors(@RequestParam(defaultValue = "") String query, Model model) { var values = query.isBlank() ? authors.findAll() : authors.findByNameContainingIgnoreCase(query); var counts = new LinkedHashMap<Long, Long>(); values.forEach(a -> counts.put(a.getId(), books.countByAuthorIgnoreCase(a.getName()))); model.addAttribute("items", values); model.addAttribute("counts", counts); model.addAttribute("kind", "Authors"); model.addAttribute("query", query); return "catalog/list"; }
    @GetMapping("/publishers") public String publishers(@RequestParam(defaultValue = "") String query, Model model) { model.addAttribute("items", query.isBlank() ? publishers.findAll() : publishers.findByNameContainingIgnoreCase(query)); model.addAttribute("counts", new LinkedHashMap<>()); model.addAttribute("kind", "Publishers"); model.addAttribute("query", query); return "catalog/list"; }
    @GetMapping("/categories/new") public String categoryForm(Model model) { model.addAttribute("item", new Category()); model.addAttribute("kind", "Category"); model.addAttribute("action", "/categories"); return "catalog/form"; }
    @GetMapping("/authors/new") public String authorForm(Model model) { model.addAttribute("item", new Author()); model.addAttribute("kind", "Author"); model.addAttribute("action", "/authors"); return "catalog/form"; }
    @GetMapping("/publishers/new") public String publisherForm(Model model) { model.addAttribute("item", new Publisher()); model.addAttribute("kind", "Publisher"); model.addAttribute("action", "/publishers"); return "catalog/form"; }
    @PostMapping("/categories") public String saveCategory(@Valid @ModelAttribute("item") Category item, BindingResult result) { if (!result.hasErrors()) categories.save(item); return "redirect:/categories"; }
    @PostMapping("/authors") public String saveAuthor(@Valid @ModelAttribute("item") Author item, BindingResult result) { if (!result.hasErrors()) authors.save(item); return "redirect:/authors"; }
    @PostMapping("/publishers") public String savePublisher(@Valid @ModelAttribute("item") Publisher item, BindingResult result) { if (!result.hasErrors()) publishers.save(item); return "redirect:/publishers"; }
    @PostMapping("/categories/{id}/delete") public String deleteCategory(@PathVariable Long id, RedirectAttributes redirect) { var item = categories.findById(id).orElseThrow(); if (books.countByCategoryIgnoreCase(item.getName()) > 0) redirect.addFlashAttribute("error", "Reassign books before deleting this category."); else categories.delete(item); return "redirect:/categories"; }
    @PostMapping("/authors/{id}/delete") public String deleteAuthor(@PathVariable Long id, RedirectAttributes redirect) { var item = authors.findById(id).orElseThrow(); if (books.countByAuthorIgnoreCase(item.getName()) > 0) redirect.addFlashAttribute("error", "Reassign books before deleting this author."); else authors.delete(item); return "redirect:/authors"; }
    @PostMapping("/publishers/{id}/delete") public String deletePublisher(@PathVariable Long id) { publishers.deleteById(id); return "redirect:/publishers"; }
}
