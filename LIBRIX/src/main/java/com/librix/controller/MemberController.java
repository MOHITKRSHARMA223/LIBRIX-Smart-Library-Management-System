package com.librix.controller;

import com.librix.model.LibraryMember;
import com.librix.repository.LibraryMemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final LibraryMemberRepository members;
    public MemberController(LibraryMemberRepository members) { this.members = members; }
    @GetMapping public String list(Model model) { model.addAttribute("members", members.findAll()); return "members/list"; }
    @GetMapping("/new") public String form(Model model) { model.addAttribute("member", new LibraryMember()); return "members/form"; }
    @PostMapping public String save(@Valid @ModelAttribute("member") LibraryMember member, BindingResult result) { if (result.hasErrors()) return "members/form"; members.save(member); return "redirect:/members"; }
}
