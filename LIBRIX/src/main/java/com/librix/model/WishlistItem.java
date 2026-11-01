package com.librix.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "wishlist_items", uniqueConstraints = @UniqueConstraint(name = "uk_wishlist_user_book", columnNames = {"user_id", "book_id"}))
public class WishlistItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private LibraryBook book;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected WishlistItem() { }
    public WishlistItem(AppUser user, LibraryBook book) { this.user = user; this.book = book; }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public LibraryBook getBook() { return book; }
    public Instant getCreatedAt() { return createdAt; }
}
