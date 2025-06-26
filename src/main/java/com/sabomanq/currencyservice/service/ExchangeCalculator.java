package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.Database;
import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeCalculator {
    private ExchangeRatesDAO ratesDAO;
    private Database currenciesDAO;

    public ExchangeCalculator(ExchangeRatesDAO ratesDAO, Database currenciesDAO) {
        this.ratesDAO = ratesDAO;
        this.currenciesDAO = currenciesDAO;
    }

    public Optional<ExchangeRate> findSuitableCourse(ExchangeTransactionForm data) throws DatabaseError {
        Optional<ExchangeRate> exchangeRate = ratesDAO.getExchangeRate(data.baseCurrency, data.targetCurrency);
        if (exchangeRate.isPresent()) {
            return exchangeRate;
        } else if ((exchangeRate = ratesDAO.getExchangeRate(data.targetCurrency, data.baseCurrency)).isPresent()){
            exchangeRate.get().invert();
            return exchangeRate;
        } else {
            return crossUSDRate(data);
        }
    }

    public Optional<ExchangeRate> crossUSDRate(ExchangeTransactionForm data) throws DatabaseError {
        Optional<ExchangeRate> usdBase = ratesDAO.getExchangeRate("USD" + data.baseCurrency);
        Optional<ExchangeRate> usdTarget = ratesDAO.getExchangeRate("USD" + data.targetCurrency);

        if (usdBase.isPresent() && usdTarget.isPresent()) {
            Currency base = currenciesDAO.getCurrency(data.baseCurrency);
            Currency target = currenciesDAO.getCurrency(data.targetCurrency);

            System.out.println("USD-A: " + usdBase.get().rate);
            System.out.println("USD-B: " + usdTarget.get().rate);

            ExchangeRate crossRate = new ExchangeRate(0, base, target,
                    usdTarget.get().rate.setScale(2, RoundingMode.DOWN).
                            divide(usdBase.get().rate, RoundingMode.DOWN));
            return Optional.of(crossRate);
        }

        return Optional.empty();
    }

    public ExchangeRateSumDTO conversionRate(ExchangeTransactionForm data) throws DatabaseError, NotFoundException {
        Optional<ExchangeRate> rate = findSuitableCourse(data);
        if (rate.isEmpty()) {
            throw new NotFoundException("No exchange rate found");
        }

        BigDecimal convertedAmount = rate.get().rate.multiply(data.amount);
        return new ExchangeRateSumDTO(rate.get(), data.amount, convertedAmount);
    }
}
