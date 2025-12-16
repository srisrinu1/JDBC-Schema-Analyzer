package com.sr.jdbc_tool;

import com.sr.jdbc_tool.config.DbConfig;
import com.sr.jdbc_tool.config.DbConfigLoader;
import com.sr.jdbc_tool.db.DataSourceFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
     private static final Logger logger=LoggerFactory.getLogger(App.class);

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
                logger.info("Both databases are reachable.");
            }
        } catch (Exception e) {
            logger.error("Startup failed: " + e.getMessage(), e);
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
