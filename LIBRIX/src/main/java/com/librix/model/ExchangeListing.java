package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "exchange_listings")
public class ExchangeListing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @NotBlank @Column(nullable = false, length = 180) private String title;
    @NotBlank @Column(nullable = false, length = 1000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private ExchangeStatus status = ExchangeStatus.OPEN;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected ExchangeListing() { }
    public ExchangeListing(AppUser user, String title, String description) { this.user = user; this.title = title; this.description = description; }
    public Long getId() { return id; } public AppUser getUser() { return user; } public String getTitle() { return title; }
    public String getDescription() { return description; } public ExchangeStatus getStatus() { return status; } public Instant getCreatedAt() { return createdAt; }
    public void close() { status = ExchangeStatus.CLOSED; }
}
