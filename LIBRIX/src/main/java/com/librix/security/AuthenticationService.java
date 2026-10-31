package com.librix.security;

import com.librix.model.Role;
import com.librix.model.UserSession;
import org.mindrot.jbcrypt.BCrypt;
import java.time.Instant;
import java.util.Map;

public final class AuthenticationService {
    private final Map<String, Account> accounts = Map.of(
        "admin", new Account(1, Role.SUPER_ADMIN, BCrypt.hashpw("admin123", BCrypt.gensalt(10))),
        "student", new Account(2, Role.STUDENT, BCrypt.hashpw("student123", BCrypt.gensalt(10)))
    );

    public UserSession authenticate(String username, char[] password) {
        if (username == null || password == null) throw new IllegalArgumentException("Credentials are required");
        Account account = accounts.get(username.trim().toLowerCase());
        if (account == null || !BCrypt.checkpw(new String(password), account.passwordHash())) {
            throw new SecurityException("Invalid username or password");
        }
        return new UserSession(account.id(), username.trim(), account.role(), Instant.now());
    }

    private record Account(long id, Role role, String passwordHash) { }
}
