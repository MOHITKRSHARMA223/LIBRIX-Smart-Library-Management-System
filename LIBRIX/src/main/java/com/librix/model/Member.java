package com.librix.model;

public record Member(long id, String fullName, String email, boolean active) {
    public Member {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Full name is required");
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new IllegalArgumentException("Valid email is required");
    }
}
