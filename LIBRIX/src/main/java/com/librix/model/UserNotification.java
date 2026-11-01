package com.librix.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_notifications")
public class UserNotification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @Column(nullable = false, length = 180) private String title;
    @Column(nullable = false, length = 1000) private String message;
    @Column(nullable = false) private boolean read;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected UserNotification() { }
    public UserNotification(AppUser user, String title, String message) { this.user = user; this.title = title; this.message = message; }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public Instant getCreatedAt() { return createdAt; }
    public void markRead() { read = true; }
}
