package com.librix.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasAnyRole('SUPER_ADMIN','LIBRARIAN')")
public class AdminRouteController {
    @GetMapping("/admin/books") public String books() { return "redirect:/books"; }
    @GetMapping("/admin/members") public String members() { return "redirect:/members"; }
    @GetMapping("/admin/transactions") public String transactions() { return "redirect:/borrowing"; }
    @GetMapping("/admin/categories") public String categories() { return "redirect:/categories"; }
    @GetMapping("/admin/authors") public String authors() { return "redirect:/authors"; }
    @GetMapping("/admin/publishers") public String publishers() { return "redirect:/publishers"; }
    @GetMapping("/admin/activity-logs") public String activity() { return "redirect:/admin/activity"; }
}
