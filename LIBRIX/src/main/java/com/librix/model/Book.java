package com.librix.model;

public record Book(long id, String isbn, String title, String author, String category, int totalCopies, int availableCopies) {
    public Book {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author is required");
        if (totalCopies < 0 || availableCopies < 0 || availableCopies > totalCopies) {
            throw new IllegalArgumentException("Copy counts are invalid");
        }
    }
    public boolean isAvailable() { return availableCopies > 0; }
}
