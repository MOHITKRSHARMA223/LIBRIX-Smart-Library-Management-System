package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "book_reviews", uniqueConstraints = @UniqueConstraint(name = "uk_review_user_book", columnNames = {"user_id", "book_id"}))
public class BookReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private LibraryBook book;
    @Min(1) @Max(5) @Column(nullable = false) private int rating;
    @NotBlank @Column(nullable = false, length = 1200) private String comment;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected BookReview() { }
    public BookReview(AppUser user, LibraryBook book, int rating, String comment) { this.user = user; this.book = book; this.rating = rating; this.comment = comment; }
    public Long getId() { return id; } public AppUser getUser() { return user; } public LibraryBook getBook() { return book; }
    public int getRating() { return rating; } public String getComment() { return comment; } public Instant getCreatedAt() { return createdAt; }
    public void update(int rating, String comment) { this.rating = rating; this.comment = comment; this.createdAt = Instant.now(); }
}
