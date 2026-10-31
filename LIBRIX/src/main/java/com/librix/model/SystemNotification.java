package com.librix.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "system_notifications", indexes = @Index(name = "idx_notifications_created", columnList = "createdAt"))
public class SystemNotification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 180) private String title;
    @Column(nullable = false, length = 2000) private String message;
    @Column(nullable = false, length = 40) private String category;
    @Column(nullable = false) private boolean read;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected SystemNotification() { }
    public SystemNotification(String title, String message, String category) { this.title = title; this.message = message; this.category = category; }
    public Long getId() { return id; } public String getTitle() { return title; } public String getMessage() { return message; } public String getCategory() { return category; } public boolean isRead() { return read; } public Instant getCreatedAt() { return createdAt; }
    public void markRead() { read = true; }
}
