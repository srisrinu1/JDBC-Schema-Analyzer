package com.sr.jdbc_tool.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(DbConfigLoader.class);

    public DbConfig load(String resourcePath) {
        Objects.requireNonNull(resourcePath, "resourcePath must not be null");

        String path = resourcePath.trim();
        if (path.isEmpty()) {
            throw new IllegalArgumentException("resourcePath must not be empty");
        }

        Properties properties = new Properties();
        logger.info("Attempting to load DB config from resource: {}", path);

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                logger.error("Resource not found on classpath: {}", path);
                throw new IllegalArgumentException("Resource not found on classpath: " + path);
            }
            properties.load(input);
            logger.info("Successfully loaded DB config from resource: {}", path);
        } catch (IOException e) {
            logger.error("Failed to load properties from resource: {}", path, e);
            throw new RuntimeException("Failed to load properties from resource: " + path, e);
        }

        String url = required(properties, "jdbc.url", path);
        String username = required(properties, "jdbc.username", path);
        String password = properties.getProperty("jdbc.password", "");
        String driverClassName = required(properties, "jdbc.driverClassName", path);
        String dialect = properties.getProperty("jdbc.dialect", "");
        
        logger.debug("DbConfig loaded: url={}, username={}, driverClassName={}, dialect={}",
                url, username, driverClassName, dialect);

        return new DbConfig(url, username, password, driverClassName, dialect);
    }

    private static String required(Properties props, String key, String resourcePath) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            logger.error("Missing required property '{}' in resource: {}", key, resourcePath);
            throw new IllegalArgumentException(
                    "Missing required property '" + key + "' in resource: " + resourcePath
            );
        }
        return value.trim();
    }
}
