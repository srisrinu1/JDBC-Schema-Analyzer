package com.sr.jdbc_tool.db;

import com.sr.jdbc_tool.config.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DataSourceFactory {

    private static final int LOGIN_TIMEOUT_SECONDS = 5;
    private static final int VALIDATION_TIMEOUT_SECONDS = 5;

    public Connection createConnection(DbConfig dbConfig) throws SQLException {
        Objects.requireNonNull(
                dbConfig,
                "DbConfig must not be null: a valid database configuration is required to create a connection"
        );

        String url = requireNonBlank(dbConfig.getUrl(), "db.url");
        String username = requireNonBlank(dbConfig.getUserName(), "db.username");
        String password = (dbConfig.getPassword() == null) ? "" : dbConfig.getPassword();
        String driverClassName = requireNonBlank(dbConfig.getDriverClassName(), "db.driverClassName");
        String dialect = (dbConfig.getDialect() == null) ? "" : dbConfig.getDialect();

        DriverManager.setLoginTimeout(LOGIN_TIMEOUT_SECONDS);
        loadDriver(driverClassName);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);

            if (!conn.isValid(VALIDATION_TIMEOUT_SECONDS)) {
                closeQuietly(conn);
                throw new SQLException(buildContextMessage(
                        "Connection created but failed validation",
                        url, username, driverClassName, dialect
                ));
            }

            conn.setAutoCommit(true);
            return conn;

        } catch (SQLException e) {
            closeQuietly(conn);
            throw new SQLException(
                    buildContextMessage("Failed to create connection", url, username, driverClassName, dialect),
                    e
            );
        }
    }

    private static void loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC driver class not found: " + driverClassName, e);
        }
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required configuration value: " + fieldName);
        }
        return value.trim();
    }

    private static String buildContextMessage(String prefix,
                                              String url,
                                              String username,
                                              String driverClassName,
                                              String dialect) {
        return prefix +
                ": url=" + safeUrl(url) +
                ", username=" + username +
                ", driverClassName=" + driverClassName +
                ", dialect=" + dialect;
    }

    private static void closeQuietly(Connection conn) {
        if (conn == null) return;
        try {
            conn.close();
        } catch (SQLException ignored) {
        }
    }

    private static String safeUrl(String url) {
        if (url == null) return "null";

        String sanitizedURL = url;

        int schemeIdx = sanitizedURL.indexOf("://");
        if (schemeIdx >= 0) {
            int atIdx = sanitizedURL.indexOf('@', schemeIdx + 3);
            if (atIdx > 0) {
                String credentialsPart = sanitizedURL.substring(schemeIdx + 3, atIdx);
                int colonIdx = credentialsPart.indexOf(':');
                if (colonIdx > 0) {
                    sanitizedURL = sanitizedURL.substring(0, schemeIdx + 3)
                            + credentialsPart.substring(0, colonIdx)
                            + ":***@"
                            + sanitizedURL.substring(atIdx + 1);
                }
            }
        }

        return sanitizedURL.replaceAll(
                "(?i)([?;&])(password|passwd|pwd|pass|secret|token|access[_-]?token|api[_-]?key)=([^;&]*)",
                "$1$2=***"
        );
    }
}
