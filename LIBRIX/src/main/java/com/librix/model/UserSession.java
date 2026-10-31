package com.librix.model;

import java.time.Instant;

public record UserSession(long userId, String username, Role role, Instant authenticatedAt) {
    public boolean canManageLibrary() {
        return role == Role.SUPER_ADMIN || role == Role.LIBRARIAN;
    }
}
