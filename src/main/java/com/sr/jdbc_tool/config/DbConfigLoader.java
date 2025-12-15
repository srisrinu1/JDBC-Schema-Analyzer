package com.sr.jdbc_tool.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class DbConfigLoader {

    public DbConfig load(String resourcePath) {
        Objects.requireNonNull(resourcePath, "resourcePath must not be null");

        String path = resourcePath.trim();
        if (path.isEmpty()) {
            throw new IllegalArgumentException("resourcePath must not be empty");
        }

        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + path);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from resource: " + path, e);
        }

        String url = required(properties, "jdbc.url", path);
        String username = required(properties, "jdbc.username", path);
        String password = properties.getProperty("jdbc.password", "");
        String driverClassName = required(properties, "jdbc.driverClassName", path);
        String dialect = properties.getProperty("jdbc.dialect", "");

        return new DbConfig(url, username, password, driverClassName, dialect);
    }

    private static String required(Properties props, String key, String resourcePath) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing required property '" + key + "' in resource: " + resourcePath
            );
        }
        return value.trim();
    }
}
