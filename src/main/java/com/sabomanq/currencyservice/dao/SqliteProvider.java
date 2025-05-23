package com.sabomanq.currencyservice.dao;

import org.flywaydb.core.Flyway;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteProvider implements ConnectionProvider{
    public Connection open() throws SQLException {

        try {
            System.out.println("Registering driver...");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Driver Registered!");
        String url = "jdbc:sqlite:mydb.db";

        boolean migrate = false;

        File dbFile = new File("mydb.db");
        if (dbFile.exists()) {
            System.out.println("Database already exists.");
        } else {
            System.out.println("Database does not exist. Creating...");
            migrate = true;
        }

        if (migrate) {
            Flyway flyway = Flyway.configure().dataSource(url, "saba", "saba").load();
            flyway.migrate();
        }

        Connection conn;
        try {
            conn = DriverManager.getConnection(url, "saba", "saba");
            System.out.println("Connection to SQLite has been established. TUTUTU");
        } catch (Exception e) {
            System.out.println("Error opening database.");
            throw new SQLException("Error opening database.");
        }

        return conn;
    }
}
