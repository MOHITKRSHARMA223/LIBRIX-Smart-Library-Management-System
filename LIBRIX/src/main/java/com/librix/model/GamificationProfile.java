package com.librix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gamification_profiles", uniqueConstraints = @UniqueConstraint(name = "uk_gamification_user", columnNames = "user_id"))
public class GamificationProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @Column(nullable = false) private int xp;
    protected GamificationProfile() { }
    public GamificationProfile(AppUser user) { this.user = user; }
    public Long getId() { return id; } public AppUser getUser() { return user; } public int getXp() { return xp; }
    public void award(int points) { xp += Math.max(0, points); }
    public String getBadge() { return xp >= 500 ? "Library legend" : xp >= 250 ? "Book champion" : xp >= 100 ? "Keen reader" : "New explorer"; }
}
