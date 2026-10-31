package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(name = "uk_categories_name", columnNames = "name"))
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false, length = 120) private String name;
    @Column(length = 500) private String description;
    public Category() { }
    public Category(String name, String description) { this.name = name; this.description = description; }
    public Long getId() { return id; } public String getName() { return name; } public String getDescription() { return description; }
    public void setName(String name) { this.name = name; } public void setDescription(String description) { this.description = description; }
}
