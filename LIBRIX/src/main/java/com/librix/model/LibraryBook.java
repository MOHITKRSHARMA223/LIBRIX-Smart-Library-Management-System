package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "books", indexes = @Index(name = "idx_books_title", columnList = "title"))
public class LibraryBook {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 30)
    private String isbn;
    @NotBlank @Column(nullable = false, length = 240)
    private String title;
    @NotBlank @Column(nullable = false, length = 160)
    private String author;
    @Column(length = 120)
    private String category;
    @Min(0) @Column(nullable = false)
    private int totalCopies;
    @Min(0) @Column(nullable = false)
    private int availableCopies;
    @Column(length = 80)
    private String shelfLocation;

    public LibraryBook() { }
    public LibraryBook(String isbn, String title, String author, String category, int totalCopies) { this.isbn = isbn; this.title = title; this.author = author; this.category = category; this.totalCopies = totalCopies; this.availableCopies = totalCopies; }
    public Long getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public String getShelfLocation() { return shelfLocation; }
    public boolean isAvailable() { return availableCopies > 0; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }
    public void borrowCopy() { if (availableCopies < 1) throw new IllegalStateException("No available copies"); availableCopies--; }
    public void returnCopy() { if (availableCopies < totalCopies) availableCopies++; }
}
