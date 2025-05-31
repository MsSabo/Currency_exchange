package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ExchangeRatesDAO {
    private ConnectionProvider connectionProvider;

    public ExchangeRatesDAO(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ArrayList<ExchangeRateFull> getExchangeRates() {
        String query =
                "SELECT \n" +
                "    er.id,\n" +
                "    er.rate,\n" +
                "    \n" +
                "    bc.id AS baseId,\n" +
                "    bc.code AS baseCode,\n" +
                "    bc.fullName AS basefullName,\n" +
                "    bc.sign AS baseSign,\n" +
                "    \n" +
                "    tc.id AS targetId,\n" +
                "    tc.code AS targetCode,\n" +
                "    tc.fullName AS targetfullName,\n" +
                "    tc.sign AS targetSign\n" +
                "\n" +
                "FROM ExchangeRates er\n" +
                "JOIN Currencies bc ON er.baseCurrencyId = bc.id\n" +
                "JOIN Currencies tc ON er.targetCurrencyId = tc.id;";

        try (Connection conn = connectionProvider.open()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<ExchangeRateFull> exchangeRates = new ArrayList<>();
            while (rs.next())
            {
                int id = rs.getInt("id");
                BigDecimal rate = rs.getBigDecimal("rate");
                int baseId = rs.getInt("baseId");
                String baseName = rs.getString("basefullName");
                String baseCode = rs.getString("baseCode");
                String baseSign = rs.getString("baseSign");

                int targetId = rs.getInt("targetId");
                String targetCode = rs.getString("targetCode");
                String targetFullName = rs.getString("targetfullName");
                String targetSign = rs.getString("targetSign");
                Currency base = new Currency(baseId, baseCode, baseName, baseSign);
                Currency target = new Currency(targetId, targetCode, targetFullName, targetSign);

                exchangeRates.add(new ExchangeRateFull(id, base, target, rate));
            }

            return exchangeRates;
        }
        catch (SQLException e)
        {
           throw new DatabaseError("Internal database error");
        }
    }

    public Optional<ExchangeRateFull> getExchangeRate(String pair) throws DatabaseError, NotFoundException {
        String query = "SELECT \n" +
                "    er.id,\n" +
                "    er.rate,\n" +
                "    \n" +
                "    bc.id AS baseId,\n" +
                "    bc.code AS baseCode,\n" +
                "    bc.fullName AS basefullName,\n" +
                "    bc.sign AS baseSign,\n" +
                "    \n" +
                "    tc.id AS targetId,\n" +
                "    tc.code AS targetCode,\n" +
                "    tc.fullName AS targetfullName,\n" +
                "    tc.sign AS targetSign\n" +
                "\n" +
                "FROM ExchangeRates er\n" +
                "JOIN Currencies bc ON er.baseCurrencyId = bc.id\n" +
                "JOIN Currencies tc ON er.targetCurrencyId = tc.id\n" +
                "WHERE (bc.code || tc.code) = ?";

        try (Connection conn = connectionProvider.open()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, pair);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("id");
                BigDecimal rate = rs.getBigDecimal("rate");
                int baseId = rs.getInt("baseId");
                String baseName = rs.getString("basefullName");
                String baseCode = rs.getString("baseCode");
                String baseSign = rs.getString("baseSign");

                int targetId = rs.getInt("targetId");
                String targetCode = rs.getString("targetCode");
                String targetFullName = rs.getString("targetfullName");
                String targetSign = rs.getString("targetSign");
                Currency base = new Currency(baseId, baseCode, baseName, baseSign);
                Currency target = new Currency(targetId, targetCode, targetFullName, targetSign);
                return Optional.of(new ExchangeRateFull(id, base, target, rate));
            } else {
                return Optional.empty();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseError("Internal database error");
        }
    }

    public Optional<ExchangeRateFull> addRate(String baseCode, String targetCode, float rate) throws DatabaseError {
        String query = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate)\n" +
                "SELECT c1.id, c2.id, ?\n" +
                "FROM Currencies c1\n" +
                "JOIN Currencies c2 ON 1 = 1\n" +
                "WHERE c1.code = ? AND c2.code = ?";
        try (Connection conn = connectionProvider.open()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBigDecimal(1, new BigDecimal(rate));
            stmt.setString(2, baseCode);
            stmt.setString(3, targetCode);
            int count = stmt.executeUpdate();
            if (count != 1)
            {
                return null;
            }
            else {
                return getExchangeRate(baseCode + targetCode);
            }
        }
        catch (SQLException e)
        {
            if (e.getErrorCode() == 19) {
                throw new UniqueConstraintViolationException(e.getMessage(), e.getCause());
            }
            throw new DatabaseError("Internal database error");
        }
    }

    public Optional<ExchangeRateFull> patchRate(String pair, float rate) throws NotFoundException, DatabaseError {
        String query = "UPDATE ExchangeRates\n" +
                "SET rate = ?\n" +
                "WHERE id IN (\n" +
                "    SELECT rates.id\n" +
                "    FROM ExchangeRates AS rates\n" +
                "    JOIN Currencies AS c1 ON rates.baseCurrencyId = c1.id\n" +
                "    JOIN Currencies AS c2 ON rates.targetCurrencyId = c2.id\n" +
                "    WHERE c1.code || c2.code = ?\n" +
                ");";

        try (Connection conn = connectionProvider.open()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBigDecimal(1, new BigDecimal(rate));
            stmt.setString(2, pair);
            int count = stmt.executeUpdate();
            if (count != 1) {
                throw new NotFoundException("The exchange rate for the pair was not found.");
            } else {
                return getExchangeRate(pair);
            }
        }
        catch (SQLException err)
        {
            throw new DatabaseError("Internal database error");
        }
    }
}
