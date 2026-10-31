package com.librix.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "activity_logs", indexes = @Index(name = "idx_activity_created", columnList = "createdAt"))
public class ActivityLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) private AppUser user;
    @Column(nullable = false, length = 80) private String action;
    @Column(nullable = false, length = 80) private String module;
    @Column(nullable = false, length = 1000) private String description;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected ActivityLog() { }
    public ActivityLog(AppUser user, String action, String module, String description) { this.user = user; this.action = action; this.module = module; this.description = description; }
    public Long getId() { return id; } public AppUser getUser() { return user; } public String getAction() { return action; } public String getModule() { return module; } public String getDescription() { return description; } public Instant getCreatedAt() { return createdAt; }
}
