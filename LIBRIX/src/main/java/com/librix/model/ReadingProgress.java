package com.librix.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reading_progress", uniqueConstraints = @UniqueConstraint(name = "uk_progress_user_book", columnNames = {"user_id", "book_id"}))
public class ReadingProgress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private LibraryBook book;
    @Column(nullable = false) private int percentComplete;
    @Column(nullable = false) private Instant updatedAt = Instant.now();
    protected ReadingProgress() { }
    public ReadingProgress(AppUser user, LibraryBook book, int percentComplete) { this.user = user; this.book = book; this.percentComplete = percentComplete; }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public LibraryBook getBook() { return book; }
    public int getPercentComplete() { return percentComplete; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void updatePercent(int percentComplete) { this.percentComplete = percentComplete; this.updatedAt = Instant.now(); }
}
