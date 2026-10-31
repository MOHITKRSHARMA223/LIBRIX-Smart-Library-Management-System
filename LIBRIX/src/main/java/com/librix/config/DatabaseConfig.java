package com.librix.config;

public record DatabaseConfig(String url, String username, String password) {
    public static DatabaseConfig fromEnvironment() {
        return new DatabaseConfig(
            value("LIBRIX_DB_URL", "jdbc:mysql://localhost:3306/librix?serverTimezone=UTC"),
            value("LIBRIX_DB_USER", ""),
            value("LIBRIX_DB_PASSWORD", "")
        );
    }

    public boolean isConfigured() {
        return !username.isBlank() && !password.isBlank();
    }

    private static String value(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
