package com.sabomanq.currencyservice.listener;

import com.sabomanq.currencyservice.dao.SqliteProvider;
import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class FlywayMigrationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("FlywayMigration:contextInitialized: " + sce.toString());

        Properties props = new Properties();
        try {
            Class.forName("org.sqlite.JDBC");
            props.load(SqliteProvider.class.getClassLoader().getResourceAsStream("flyway.conf"));
        } catch (ClassNotFoundException | IOException e ) {
            System.out.println("ContextInitialized: " + e);
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
            throw new RuntimeException("Error migrating database: " + err.getMessage());
        }

        System.out.println("FlywayMigrationListener: DATABASE migration success.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("FlywayMigration:contextDestroyed: " + sce.toString());
    }
}
