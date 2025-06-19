package com.sabomanq.currencyservice.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqliteProvider implements ConnectionProvider{
    public Connection open() throws SQLException {

        Properties props = new Properties();
        try {
            props.load(SqliteProvider.class.getClassLoader().getResourceAsStream("flyway.conf"));
        } catch (IOException e ) {
            System.out.println("SqliteProvider:open: " + e);
        }

        return DriverManager.getConnection(props.getProperty("flyway.url"),
                                           props.getProperty("flyway.user"),
                                           props.getProperty("flyway.password"));
    }
}
