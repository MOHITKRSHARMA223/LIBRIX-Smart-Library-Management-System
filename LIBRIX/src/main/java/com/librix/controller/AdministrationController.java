package com.librix.controller;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequestMapping("/admin")
public class AdministrationController {
    private final ActivityLogRepository logs; private final SystemNotificationRepository notifications; private final LibrarySettingRepository settings;
    public AdministrationController(ActivityLogRepository logs, SystemNotificationRepository notifications, LibrarySettingRepository settings) { this.logs = logs; this.notifications = notifications; this.settings = settings; }
    @GetMapping("/activity") public String activity(Model model) { model.addAttribute("logs", logs.findTop100ByOrderByCreatedAtDesc()); return "admin/activity"; }
    @GetMapping("/notifications") public String notifications(Model model) { model.addAttribute("notifications", notifications.findTop100ByOrderByCreatedAtDesc()); return "admin/notifications"; }
    @PostMapping("/notifications/{id}/read") public String markRead(@PathVariable Long id) { var item = notifications.findById(id).orElseThrow(); item.markRead(); notifications.save(item); return "redirect:/admin/notifications"; }
    @GetMapping("/settings") public String settings(Model model) { model.addAttribute("settings", settings.findAll()); return "admin/settings"; }
    @PostMapping("/settings") public String saveSetting(@RequestParam String settingKey, @RequestParam String settingValue) { settings.save(new LibrarySetting(settingKey, settingValue)); return "redirect:/admin/settings"; }
}
