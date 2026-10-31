package com.librix.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private final DatabaseConfig config;

    public ConnectionFactory(DatabaseConfig config) { this.config = config; }

    public Connection open() throws SQLException {
        if (!config.isConfigured()) throw new SQLException("Database credentials are not configured");
        return DriverManager.getConnection(config.url(), config.username(), config.password());
    }
}
