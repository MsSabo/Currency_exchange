package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeCalculator {
    private ExchangeRatesDAO ratesDAO;

    public ExchangeCalculator(ExchangeRatesDAO ratesDAO) {
        this.ratesDAO = ratesDAO;
    }

    public ExchangeRateSumDTO calculateExchangeSum(ExchangeTransactionForm form) throws NotFoundException, DatabaseError {
/*
        В таблице ExchangeRates существует валютная пара AB - берём её курс
        В таблице ExchangeRates существует валютная пара BA - берем её курс, и считаем обратный, чтобы получить AB
        В таблице ExchangeRates существуют валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
*/
        Optional<ExchangeRateFull> rate = ratesDAO.getExchangeRate(form.baseCurrency + form.targetCurrency);
        if (rate.isPresent()) {
            BigDecimal convertedAmount = rate.get().exchangeRate.multiply(form.amount);
            ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
            return result;
        } else if ((rate = ratesDAO.getExchangeRate(form.targetCurrency + form.baseCurrency)).isPresent()){
            BigDecimal one = BigDecimal.ONE; // аналог 1
            BigDecimal rateValue = rate.get().exchangeRate; // предположим, это BigDecimal
            BigDecimal amount = form.amount; // тоже BigDecimal

            BigDecimal convertedAmount = one.divide(rateValue, 10, RoundingMode.HALF_UP)
                    .multiply(amount);
            ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
            return result;
        } else {
            Optional<ExchangeRateFull> usdBase = ratesDAO.getExchangeRate("USD" + form.baseCurrency);
            Optional<ExchangeRateFull> usdTarget = ratesDAO.getExchangeRate("USD" + form.targetCurrency);
            if (usdBase.isPresent() && usdTarget.isPresent()) {
                BigDecimal convertedAmount = ((usdTarget.get().exchangeRate.divide(usdBase.get().exchangeRate)).multiply(form.amount));
                ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
                return result;
            } else {
                throw new NotFoundException("No exchange rate found");
            }
        }
    }
}
