package com.sabomanq.currencyservice.dao;

import org.flywaydb.core.Flyway;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteProvider implements ConnectionProvider{
    public Connection open() throws SQLException {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:sqlite:mydb.db";
        boolean migrate = false;

        File dbFile = new File("mydb.db");
        if (!dbFile.exists()) {
            migrate = true;
        }

        try {
            if (migrate) {
                Flyway flyway = Flyway.configure().dataSource(url, "saba", "saba").load();
                flyway.migrate();
            }
        } catch (org.flywaydb.core.api.FlywayException err) {
            throw new SQLException("Error migrating database: " + err.getMessage());
        }

        return DriverManager.getConnection(url, "saba", "saba");
    }
}
