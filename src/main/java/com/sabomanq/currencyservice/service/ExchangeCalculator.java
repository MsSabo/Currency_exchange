package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.Database;
import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;

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

    public ExchangeRateSumDTO calculateExchangeSum(ExchangeTransactionForm form) throws NotFoundException, DatabaseError {
/*
        В таблице ExchangeRates существует валютная пара AB - берём её курс
        В таблице ExchangeRates существует валютная пара BA - берем её курс, и считаем обратный, чтобы получить AB
        В таблице ExchangeRates существуют валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
*/
        Optional<ExchangeRate> rate = ratesDAO.getExchangeRate(form.baseCurrency + form.targetCurrency);
        if (rate.isPresent()) {
            BigDecimal convertedAmount = rate.get().rate.multiply(form.amount);
            return new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
        } else if ((rate = ratesDAO.getExchangeRate(form.targetCurrency + form.baseCurrency)).isPresent()){
            rate.get().invert();
            BigDecimal convertedAmount = rate.get().rate
                    .multiply(form.amount);
            return new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
        } else {
            Optional<ExchangeRate> usdBase = ratesDAO.getExchangeRate("USD" + form.baseCurrency);
            Optional<ExchangeRate> usdTarget = ratesDAO.getExchangeRate("USD" + form.targetCurrency);
            if (usdBase.isPresent() && usdTarget.isPresent()) {
                Currency base = currenciesDAO.getCurrency(form.baseCurrency);
                Currency target = currenciesDAO.getCurrency(form.targetCurrency);

                System.out.println("USD-A: " + usdBase.get().rate);
                System.out.println("USD-B: " + usdTarget.get().rate);

                ExchangeRate crossRate = new ExchangeRate(0, base, target,
                                        (((usdTarget.get().rate).setScale(2, RoundingMode.DOWN).
                                                divide(usdBase.get().rate, RoundingMode.DOWN))));
                System.out.println("CROSS_RATE: " + crossRate.rate);
                BigDecimal convertedAmount = crossRate.rate.multiply(form.amount);
                return new ExchangeRateSumDTO(crossRate, form.amount, convertedAmount);
            } else {
                throw new NotFoundException("No exchange rate found");
            }
        }
    }
}
