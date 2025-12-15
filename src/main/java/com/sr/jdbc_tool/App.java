package com.sr.jdbc_tool;

import com.sr.jdbc_tool.config.DbConfig;
import com.sr.jdbc_tool.config.DbConfigLoader;
import com.sr.jdbc_tool.db.DataSourceFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        DbConfigLoader loader = new DbConfigLoader();
        DataSourceFactory factory = new DataSourceFactory();
        try {
            System.out.println("Loading database configurations...");
            
            DbConfig sourceConfig = loader.load("db/source-db.properties");
            DbConfig targetConfig = loader.load("db/target-db.properties");
            
            System.out.println("Configurations loaded successfully.");
            System.out.println();
            
            // Test source database connection
            System.out.println("Testing SOURCE database connection...");
            try (Connection sourceConn = factory.createConnection(sourceConfig)) {
                sanityCheck(sourceConn);
                System.out.println("✓ SUCCESS: Connected to SOURCE database");
                System.out.println("  URL: " + sourceConfig.getUrl());
                System.out.println("  User: " + sourceConfig.getUserName());
                System.out.println();
            }
            
            // Test target database connection
            System.out.println("Testing TARGET database connection...");
            try (Connection targetConn = factory.createConnection(targetConfig)) {
                sanityCheck(targetConn);
                System.out.println("✓ SUCCESS: Connected to TARGET database");
                System.out.println("  URL: " + targetConfig.getUrl());
                System.out.println("  User: " + targetConfig.getUserName());
                System.out.println();
            }
            
            System.out.println("=====================================");
            System.out.println("✓ All database connections successful");
            System.out.println("=====================================");
            
        } catch (Exception e) {
            System.err.println();
            System.err.println("=====================================");
            System.err.println("✗ FAILURE: Database connection test failed");
            System.err.println("=====================================");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void sanityCheck(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (!rs.next()) {
                throw new IllegalStateException("Sanity query returned no result");
            }
        }
    }
}
