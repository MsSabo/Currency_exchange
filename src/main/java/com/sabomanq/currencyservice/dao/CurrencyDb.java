package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;
import org.flywaydb.core.Flyway;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDb implements Database{
    private final ConnectionProvider connectionProvider;

    public CurrencyDb(ConnectionProvider connectionProvider) {
        migrate();
        this.connectionProvider = connectionProvider;
    }

    public Currency getCurrency(String code) throws DatabaseError, NotFoundException {
        System.out.println("get currency by code : " + code);

        String query = "SELECT * FROM Currencies WHERE CODE = ?";

        try (Connection connection = connectionProvider.open()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("get currency by code which has id : " + id);
                String name = rs.getString("fullName");
                String cd = rs.getString("code");
                String sign = rs.getString("sign");
                return new Currency(id, cd, name, sign);
            } else {
                throw new NotFoundException("Currency with code " + code + " not found");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatabaseError("Database internal error");
        }
    }

    public List<Currency> getCurrencies() throws DatabaseError{
        System.out.println("getCurrencies called");
        List<Currency> currencies = new ArrayList<>();
        String query = "SELECT * FROM currencies";

        try (Connection conn = connectionProvider.open()) {
            assert conn != null;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("fullName");
                String code = rs.getString("code");
                String sign = rs.getString("sign");
                System.out.println(id + " " + name + " " + code + " " + sign);
                currencies.add(new Currency(id, code, name, sign));
            }

            return currencies;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new DatabaseError("Database internal error");
        }
    }

    @Override
    public Currency addCurrency(Currency currency) throws UniqueConstraintViolationException, DatabaseError {
        System.out.println("addCurrency called");
        String query = "INSERT INTO currencies(fullName, code, sign) VALUES (?, ?, ?)";

        PreparedStatement stmt;

        try (Connection conn = connectionProvider.open()) {
            ResultSet id;
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, currency.name);
            stmt.setString(2, currency.code);
            stmt.setString(3, currency.sign);
            int rowsCount = stmt.executeUpdate();

            if (rowsCount == 0)
            {
                return null;
            } else {
                id = stmt.getGeneratedKeys();
                if (id != null && id.next()) {
                    int idx = id.getInt(1);
                    System.out.println("Row added with ID: " + idx);
                    return new Currency(idx, currency.code, currency.name, currency.sign);
                } else {
                    System.out.println("Failed to add row ID");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error code = " + e.getErrorCode());
            if (e.getErrorCode() == 19) {
                throw new UniqueConstraintViolationException(e.getMessage(), e.getCause());
            }
            throw new DatabaseError("Database internal error");
        }

        return null;
    }

    private void migrate() {
        String url = "jdbc:sqlite:mydb.db";
        boolean migrate = false;
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current directory: " + currentDir);

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
    }
}
