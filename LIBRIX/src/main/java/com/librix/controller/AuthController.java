package com.librix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login") public String login() { return "redirect:/student/login"; }
    @GetMapping("/student/login") public String studentLogin() { return "auth/student-login"; }
    @GetMapping("/admin/login") public String adminLogin() { return "auth/admin-login"; }
}
