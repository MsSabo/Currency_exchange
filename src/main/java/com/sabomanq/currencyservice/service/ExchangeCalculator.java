package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.http.Util;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateInfo;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;

import java.sql.SQLException;
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
        Optional<ExchangeRateInfo> rate = ratesDAO.getExchangeRate(form.baseCurrency + form.targetCurrency);
        if (rate.isPresent()) {
            Float convertedAmount = Float.valueOf(rate.get().rate * form.amount);
            ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
            return result;
        } else if ((rate = ratesDAO.getExchangeRate(form.targetCurrency + form.baseCurrency)).isPresent()){
            Float convertedAmount = Float.valueOf((1 / rate.get().rate) * form.amount);
            ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
            return result;
        } else {
            Optional<ExchangeRateInfo> usdBase = ratesDAO.getExchangeRate("USD" + form.baseCurrency);
            Optional<ExchangeRateInfo> usdTarget = ratesDAO.getExchangeRate("USD" + form.targetCurrency);
            if (usdBase.isPresent() && usdTarget.isPresent()) {
                Float convertedAmount = Float.valueOf((usdTarget.get().rate/usdBase.get().rate) * form.amount);
                ExchangeRateSumDTO result = new ExchangeRateSumDTO(rate.get(), form.amount, convertedAmount);
                return result;
            } else {
                throw new NotFoundException("No exchange rate found");
            }
        }
    }

    private boolean rateExists(String pair) {
        try {
            ratesDAO.getExchangeRate(pair);
            return true;
        } catch (Exception err) {
            return false;
        }
    }
}
