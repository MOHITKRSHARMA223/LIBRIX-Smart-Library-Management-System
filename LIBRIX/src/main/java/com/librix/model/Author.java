package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "authors", uniqueConstraints = @UniqueConstraint(name = "uk_authors_name", columnNames = "name"))
public class Author {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable = false, length = 160) private String name;
    @Column(length = 80) private String country;
    private LocalDate birthDate;
    @Column(length = 2000) private String biography;
    public Author() { }
    public Author(String name) { this.name = name; }
    public Long getId() { return id; } public String getName() { return name; } public String getCountry() { return country; } public LocalDate getBirthDate() { return birthDate; } public String getBiography() { return biography; }
    public String getDescription() { return biography; }
    public void setName(String name) { this.name = name; } public void setCountry(String country) { this.country = country; } public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; } public void setBiography(String biography) { this.biography = biography; }
    public void setDescription(String description) { this.biography = description; }
}
