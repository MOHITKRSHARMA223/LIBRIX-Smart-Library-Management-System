package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "members", uniqueConstraints = @UniqueConstraint(name = "uk_members_email", columnNames = "email"))
public class LibraryMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Column(nullable = false, length = 160)
    private String fullName;
    @Email @NotBlank @Column(nullable = false, length = 190)
    private String email;
    @OneToOne(fetch = FetchType.LAZY)
    private AppUser user;
    @Column(length = 40)
    private String phone;
    @Column(nullable = false)
    private boolean active = true;

    public LibraryMember() { }
    public LibraryMember(String fullName, String email, String phone) { this.fullName = fullName; this.email = email; this.phone = phone; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public AppUser getUser() { return user; }
    public boolean isActive() { return active; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setUser(AppUser user) { this.user = user; }
    public void setActive(boolean active) { this.active = active; }
}
