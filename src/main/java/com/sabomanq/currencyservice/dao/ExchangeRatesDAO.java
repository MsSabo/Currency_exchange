package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class ExchangeRatesDAO {
    private final ConnectionProvider connectionProvider;

    public ExchangeRatesDAO(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ArrayList<ExchangeRate> getExchangeRates() throws DatabaseError {
        String query = """
                       SELECT
                           er.id,
                           er.rate,
                           bc.id AS baseId,
                           bc.code AS baseCode,
                           bc.fullName AS basefullName,
                           bc.sign AS baseSign,
                           tc.id AS targetId,
                           tc.code AS targetCode,
                           tc.fullName AS targetfullName,
                           tc.sign AS targetSign
                       FROM ExchangeRates er
                       JOIN Currencies bc ON er.baseCurrencyId = bc.id
                       JOIN Currencies tc ON er.targetCurrencyId = tc.id;
                       """;

        try (Connection conn = connectionProvider.open()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();

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

                exchangeRates.add(new ExchangeRate(id, base, target, rate));
            }

            return exchangeRates;
        } catch (SQLException e) {
           throw new DatabaseError("Internal database error");
        }
    }

    public Optional<ExchangeRate> getExchangeRate(String base, String target) throws DatabaseError {
        return getExchangeRate(base + target);
    }

    public Optional<ExchangeRate> getExchangeRate(String pair) throws DatabaseError {
        String query = """
                       SELECT
                           er.id,
                           er.rate,
                           bc.id AS baseId,
                           bc.code AS baseCode,
                           bc.fullName AS basefullName,
                           bc.sign AS baseSign,
                           tc.id AS targetId,
                           tc.code AS targetCode,
                           tc.fullName AS targetfullName,
                           tc.sign AS targetSign
                       FROM ExchangeRates er
                       JOIN Currencies bc ON er.baseCurrencyId = bc.id
                       JOIN Currencies tc ON er.targetCurrencyId = tc.id
                       WHERE (bc.code || tc.code) = ?
                       """;

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
                return Optional.of(new ExchangeRate(id, base, target, rate));
            } else {
                return Optional.empty();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseError("Internal database error");
        }
    }

    public ExchangeRate addRate(String baseCode, String targetCode, float rate) throws DatabaseError {
        String query = """
                       INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate)
                           VALUES (
                               (SELECT id FROM Currencies WHERE code = ?),
                               (SELECT id FROM Currencies WHERE code = ?),
                               ?
                           )
                       """;

        try (Connection conn = connectionProvider.open()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            stmt.setBigDecimal(3, new BigDecimal(rate));
            int count = stmt.executeUpdate();
            if (count != 1)
            {
                throw new DatabaseError("Failed to insert rate");
            } else {
                return getExchangeRate(baseCode + targetCode).orElseThrow(() -> new DatabaseError("Inserted rate not found."));
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new UniqueConstraintViolationException(e.getMessage(), e.getCause());
            }
            throw new DatabaseError("Internal database error");
        }
    }

    public ExchangeRate patchRate(String pair, float rate) throws NotFoundException, DatabaseError {
        String query = """
                       UPDATE ExchangeRates
                       SET rate = ?
                       WHERE id IN (
                           SELECT rates.id
                           FROM ExchangeRates AS rates
                           JOIN Currencies AS c1 ON rates.baseCurrencyId = c1.id
                           JOIN Currencies AS c2 ON rates.targetCurrencyId = c2.id
                           WHERE c1.code || c2.code = ?
                       );
                       """;

        try (Connection conn = connectionProvider.open()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setBigDecimal(1, new BigDecimal(rate));
            stmt.setString(2, pair);
            if (stmt.executeUpdate() != 1) {
                throw new NotFoundException("The exchange rate for the pair was not found.");
            } else {
                return getExchangeRate(pair).orElseThrow(() -> new DatabaseError("Updated pair not found."));
            }
        } catch (SQLException err) {
            throw new DatabaseError("Internal database error");
        }
    }
}
