package com.librix.model;

public enum Role {
    SUPER_ADMIN("Super Admin"), LIBRARIAN("Librarian"), STUDENT("Student/Member");

    private final String label;
    Role(String label) { this.label = label; }
    public String label() { return label; }
}
