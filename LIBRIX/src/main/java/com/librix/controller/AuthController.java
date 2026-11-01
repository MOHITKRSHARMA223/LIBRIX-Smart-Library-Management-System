package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import com.librix.web.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final AppUserRepository users;
    private final LibraryMemberRepository members;
    private final ActivityLogRepository activityLogs;
    private final UserNotificationRepository notifications;
    private final PasswordEncoder encoder;

    public AuthController(AppUserRepository users, LibraryMemberRepository members, ActivityLogRepository activityLogs,
                          UserNotificationRepository notifications, PasswordEncoder encoder) {
        this.users = users; this.members = members; this.activityLogs = activityLogs;
        this.notifications = notifications; this.encoder = encoder;
    }

    @GetMapping("/login") public String login() { return "redirect:/student/login"; }
    @GetMapping("/student/login") public String studentLogin() { return "auth/student-login"; }
    @GetMapping("/admin/login") public String adminLogin() { return "auth/admin-login"; }
    @GetMapping("/student/register") public String register(Model model) {
        model.addAttribute("registration", new RegistrationForm());
        return "auth/student-register";
    }

    @PostMapping("/student/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationForm form, BindingResult result, Model model) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "registration.passwordMismatch", "Passwords do not match.");
        }
        if (users.existsByUsernameIgnoreCase(form.getUsername())) {
            result.rejectValue("username", "registration.usernameTaken", "That username is already in use.");
        }
        if (members.findByEmailIgnoreCase(form.getEmail()).isPresent()) {
            result.rejectValue("email", "registration.emailTaken", "That email is already registered.");
        }
        if (result.hasErrors()) return "auth/student-register";

        AppUser user = new AppUser(form.getUsername().trim(), encoder.encode(form.getPassword()), Role.STUDENT);
        user.setActive(false);
        users.save(user);
        LibraryMember member = new LibraryMember(form.getFullName().trim(), form.getEmail().trim().toLowerCase(), form.getPhone());
        member.setUser(user);
        members.save(member);
        activityLogs.save(new ActivityLog(user, "REGISTRATION_SUBMITTED", "AUTH",
                "Student registration submitted and is waiting for staff approval."));
        notifications.save(new UserNotification(user, "Registration received",
                "Your student account is waiting for library staff approval. We will notify you when it is ready."));
        return "redirect:/student/login?registered";
    }
}
