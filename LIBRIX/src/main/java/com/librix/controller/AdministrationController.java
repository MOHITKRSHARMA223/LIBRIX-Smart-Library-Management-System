package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import com.librix.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/admin")
public class AdministrationController {
    private final ActivityLogRepository logs; private final SystemNotificationRepository notifications; private final LibrarySettingRepository settings; private final BookReservationRepository reservations; private final ReservationService reservationService;
    public AdministrationController(ActivityLogRepository logs, SystemNotificationRepository notifications, LibrarySettingRepository settings, BookReservationRepository reservations, ReservationService reservationService) { this.logs = logs; this.notifications = notifications; this.settings = settings; this.reservations = reservations; this.reservationService = reservationService; }
    @GetMapping("/activity") public String activity(Model model) { model.addAttribute("logs", logs.findTop100ByOrderByCreatedAtDesc()); return "admin/activity"; }
    @GetMapping("/notifications") public String notifications(Model model) { model.addAttribute("notifications", notifications.findTop100ByOrderByCreatedAtDesc()); return "admin/notifications"; }
    @PostMapping("/notifications/{id}/read") public String markRead(@PathVariable Long id) { var item = notifications.findById(id).orElseThrow(); item.markRead(); notifications.save(item); return "redirect:/admin/notifications"; }
    @GetMapping("/settings") public String settings(Model model) { model.addAttribute("settings", settings.findAll()); return "admin/settings"; }
    @PostMapping("/settings") public String saveSetting(@RequestParam String settingKey, @RequestParam String settingValue) { settings.save(new LibrarySetting(settingKey, settingValue)); return "redirect:/admin/settings"; }
    @GetMapping("/reservations") public String reservations(Model model) { model.addAttribute("reservations", reservations.findTop100ByOrderByRequestedAtDesc()); return "admin/reservations"; }
    @PostMapping("/reservations/{id}/ready") public String markReservationReady(@PathVariable Long id) { reservationService.markReady(id); return "redirect:/admin/reservations"; }
    @PostMapping("/reservations/{id}/cancel") public String cancelReservation(@PathVariable Long id) { reservationService.cancelByAdmin(id); return "redirect:/admin/reservations"; }
}
