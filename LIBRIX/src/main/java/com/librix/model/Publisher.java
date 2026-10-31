package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "publishers", uniqueConstraints = @UniqueConstraint(name = "uk_publishers_name", columnNames = "name"))
public class Publisher {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false, length = 160) private String name;
    @Column(length = 190) private String website;
    @Column(length = 500) private String address;
    @Column(length = 80) private String contact;
    public Publisher() { }
    public Publisher(String name) { this.name = name; }
    public Long getId() { return id; } public String getName() { return name; } public String getWebsite() { return website; } public String getAddress() { return address; } public String getContact() { return contact; }
    public String getDescription() { return address; }
    public void setName(String name) { this.name = name; } public void setWebsite(String website) { this.website = website; } public void setAddress(String address) { this.address = address; } public void setContact(String contact) { this.contact = contact; }
    public void setDescription(String description) { this.address = description; }
}
