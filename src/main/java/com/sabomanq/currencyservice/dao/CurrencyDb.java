package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDb implements Database {
    private final ConnectionProvider connectionProvider;

    public CurrencyDb(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Currency getCurrency(String code) throws DatabaseError, NotFoundException {
        String query = "SELECT * FROM Currencies WHERE CODE = ?";

        try (Connection connection = connectionProvider.open()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("fullName");
                String cd = rs.getString("code");
                String sign = rs.getString("sign");
                return new Currency(id, cd, name, sign);
            } else {
                throw new NotFoundException("Currency with code " + code + " not found");
            }
        }
        catch (SQLException e) {
            throw new DatabaseError("Failed to get currency: " + code);
        }
    }

    public List<Currency> getCurrencies() throws DatabaseError {
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
                currencies.add(new Currency(id, code, name, sign));
            }

            return currencies;
        } catch (SQLException err) {
            throw new DatabaseError("Failed to get currencies");
        }
    }

    @Override
    public Currency addCurrency(Currency currency) throws UniqueConstraintViolationException, DatabaseError {
        String query = "INSERT INTO currencies(fullName, code, sign) VALUES (?, ?, ?)";

        try (Connection conn = connectionProvider.open()) {
            ResultSet id;
            PreparedStatement stmt;
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, currency.name);
            stmt.setString(2, currency.code);
            stmt.setString(3, currency.sign);
            int rowsCount = stmt.executeUpdate();

            if (rowsCount == 0)
            {
                throw new DatabaseError("Failed to insert currency.");
            } else {
                id = stmt.getGeneratedKeys();
                if (id != null && id.next()) {
                    int idx = id.getInt(1);
                    System.out.println("Row added with ID: " + idx);
                    return new Currency(idx, currency.code, currency.name, currency.sign);
                } else {
                    throw new DatabaseError("Currency inserted, but no ID was generated.");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new UniqueConstraintViolationException(e.getMessage(), e.getCause());
            }
            throw new DatabaseError("Database internal error.");
        }
    }
}
