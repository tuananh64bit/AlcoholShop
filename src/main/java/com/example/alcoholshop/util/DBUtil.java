package com.example.alcoholshop.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database utility class using HikariCP connection pool
 */
public class DBUtil {
    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static HikariDataSource dataSource;

    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Initialize HikariCP data source with configuration from properties file
     * Attempts configured DB (MariaDB). If unavailable, falls back to an in-memory H2 DB
     * seeded from classpath resource `db/alcohol_shop.sql` so the app can run locally without MariaDB.
     */
    private static void initializeDataSource() throws IOException {
        Properties props = new Properties();

        // Load properties from application.properties
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        }

        // Get database configuration from environment variables or properties file
        String dbUrl = getEnvOrProperty("DB_URL", props.getProperty("db.url"));
        String dbUser = getEnvOrProperty("DB_USER", props.getProperty("db.user"));
        String dbPassword = getEnvOrProperty("DB_PASSWORD", props.getProperty("db.password"));

        // If dbUrl is provided, try to initialize configured DB (prefer MariaDB)
        if (dbUrl != null && !dbUrl.trim().isEmpty()) {
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(dbUrl);
                if (dbUser != null) config.setUsername(dbUser);
                if (dbPassword != null) config.setPassword(dbPassword);

                // determine driver class: property overrides detection; otherwise infer from URL
                String driverFromProps = props.getProperty("db.driverClassName");
                String driverClassName;
                if (driverFromProps != null && !driverFromProps.trim().isEmpty()) {
                    driverClassName = driverFromProps.trim();
                } else if (dbUrl.startsWith("jdbc:mariadb:") || dbUrl.contains("mariadb")) {
                    driverClassName = "org.mariadb.jdbc.Driver";
                } else if (dbUrl.startsWith("jdbc:mysql:") || dbUrl.contains("mysql")) {
                    // Use MariaDB driver for MySQL-compatible URLs to avoid requiring mysql-connector
                    driverClassName = "org.mariadb.jdbc.Driver";
                } else {
                    // Default to MariaDB driver for MySQL-compatible servers
                    driverClassName = "org.mariadb.jdbc.Driver";
                }
                config.setDriverClassName(driverClassName);

                // Pool configuration
                config.setMaximumPoolSize(getIntProperty(props, "db.pool.maximumPoolSize", 20));
                config.setMinimumIdle(getIntProperty(props, "db.pool.minimumIdle", 5));
                config.setConnectionTimeout(getLongProperty(props, "db.pool.connectionTimeout", 30000));
                config.setIdleTimeout(getLongProperty(props, "db.pool.idleTimeout", 600000));
                config.setMaxLifetime(getLongProperty(props, "db.pool.maxLifetime", 1800000));

                config.setPoolName("AlcoholShop-Pool");
                config.setConnectionTestQuery("SELECT 1");
                config.setValidationTimeout(5000);
                config.setLeakDetectionThreshold(60000);

                HikariDataSource ds = new HikariDataSource(config);

                // Quick validation connection
                try (Connection conn = ds.getConnection()) {
                    if (!conn.isValid(2)) {
                        throw new SQLException("Configured DB connection is not valid");
                    }
                }

                dataSource = ds;
                logger.info("Database connection pool initialized successfully using configured DB: {} (driver={})", dbUrl, driverClassName);
                return;
            } catch (Exception ex) {
                logger.warn("Failed to initialize configured DB ({}). Falling back to in-memory H2 DB. Cause: {}", dbUrl, ex.getMessage());
                // fall through to H2 fallback
            }
        } else {
            logger.info("No configured DB URL found; using in-memory H2 fallback for local development");
        }

        // Fallback: initialize an in-memory H2 database preloaded with schema/data from resources
        try {
            HikariConfig h2Config = new HikariConfig();
            // Use MODE=MySQL for compatibility with MySQL SQL syntax where possible
            String h2Url = "jdbc:h2:mem:alcohol_shop;DB_CLOSE_DELAY=-1;MODE=MySQL";
            h2Config.setJdbcUrl(h2Url);
            h2Config.setUsername("sa");
            h2Config.setPassword("");
            h2Config.setDriverClassName("org.h2.Driver");

            h2Config.setMaximumPoolSize(getIntProperty(props, "db.pool.maximumPoolSize", 20));
            h2Config.setMinimumIdle(getIntProperty(props, "db.pool.minimumIdle", 5));
            h2Config.setConnectionTimeout(getLongProperty(props, "db.pool.connectionTimeout", 30000));
            h2Config.setIdleTimeout(getLongProperty(props, "db.pool.idleTimeout", 600000));
            h2Config.setMaxLifetime(getLongProperty(props, "db.pool.maxLifetime", 1800000));

            h2Config.setPoolName("AlcoholShop-H2-Pool");

            HikariDataSource h2ds = new HikariDataSource(h2Config);

            // Run init SQL from classpath resource db/alcohol_shop.sql
            try (Connection conn = h2ds.getConnection()) {
                runSqlScriptFromResource(conn, "db/alcohol_shop.sql");
            }

            dataSource = h2ds;
            logger.info("Initialized in-memory H2 fallback DB (alcohol_shop) and seeded schema/data from resources");
        } catch (Exception e) {
            logger.error("Failed to initialize H2 fallback database", e);
            throw new IOException("Failed to initialize any database", e);
        }
    }

    private static void runSqlScriptFromResource(Connection conn, String resourcePath) throws IOException, SQLException {
        InputStream is = DBUtil.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            logger.warn("SQL resource not found on classpath: {}", resourcePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (Statement stmt = conn.createStatement()) {
                while ((line = reader.readLine()) != null) {
                    // remove SQL comments
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sb.append(line).append('\n');
                    if (trimmed.endsWith(";")) {
                        // execute statement
                        String sql = sb.toString().trim();
                        // Remove trailing semicolon
                        if (sql.endsWith(";")) sql = sql.substring(0, sql.length() - 1);
                        if (!sql.isEmpty()) {
                            stmt.execute(sql);
                        }
                        sb.setLength(0);
                    }
                }
                // execute remaining
                String remaining = sb.toString().trim();
                if (!remaining.isEmpty()) {
                    String sql = remaining;
                    if (sql.endsWith(";")) sql = sql.substring(0, sql.length() - 1);
                    if (!sql.isEmpty()) stmt.execute(sql);
                }
            }
        }
    }

    /**
     * Get environment variable or fallback to property value
     */
    private static String getEnvOrProperty(String envVar, String propertyValue) {
        String envValue = System.getenv(envVar);
        return envValue != null ? envValue : propertyValue;
    }

    /**
     * Get integer property with default value
     */
    private static int getIntProperty(Properties props, String key, int defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    /**
     * Get long property with default value
     */
    private static long getLongProperty(Properties props, String key, long defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Long.parseLong(value) : defaultValue;
    }

    /**
     * Get database connection from pool
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Close database connection pool
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    /**
     * Check if data source is running
     */
    public static boolean isDataSourceRunning() {
        return dataSource != null && !dataSource.isClosed();
    }

    /**
     * Get connection pool statistics
     */
    public static String getPoolStats() {
        if (dataSource == null || dataSource.isClosed()) {
            return "DataSource not available";
        }

        return String.format("Pool: %d active, %d idle, %d total",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections());
    }
}
