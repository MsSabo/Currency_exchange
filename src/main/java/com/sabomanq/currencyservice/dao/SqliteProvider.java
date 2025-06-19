package com.sabomanq.currencyservice.dao;

import org.flywaydb.core.Flyway;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqliteProvider implements ConnectionProvider{
    public Connection open() throws SQLException {

        Properties props = new Properties();
        try {
            Class.forName("org.sqlite.JDBC");
            props.load(SqliteProvider.class.getClassLoader().getResourceAsStream("flyway.conf"));
        } catch (ClassNotFoundException | IOException e ) {
            System.out.println("SqliteProvider:open: " + e);
        }

        String url = props.getProperty("flyway.url");
        boolean migrate = false;

        File dbFile = new File(props.getProperty("flyway.dbname"));
        if (!dbFile.exists()) {
            migrate = true;
        }

        try {
            if (migrate) {
                Flyway flyway = Flyway.configure().dataSource(url,
                                props.getProperty("flyway.user"), props.getProperty("flyway.password")).load();
                flyway.migrate();
            }
        } catch (org.flywaydb.core.api.FlywayException err) {
            throw new SQLException("Error migrating database: " + err.getMessage());
        }

        return DriverManager.getConnection(url, props.getProperty("flyway.user"), props.getProperty("flyway.password"));
    }
}
