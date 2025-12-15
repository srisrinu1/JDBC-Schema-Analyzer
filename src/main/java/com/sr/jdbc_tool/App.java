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
            DbConfig sourceConfig = loader.load("db/source-db.properties");
            DbConfig targetConfig = loader.load("db/target-db.properties");
            try (
                Connection sourceConn = factory.createConnection(sourceConfig);
                Connection targetConn = factory.createConnection(targetConfig)
            ) {
                sanityCheck(sourceConn);
                sanityCheck(targetConn);
                System.out.println("Both databases are reachable.");
            }
        } catch (Exception e) {
            System.err.println("Startup failed: " + e.getMessage());
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
