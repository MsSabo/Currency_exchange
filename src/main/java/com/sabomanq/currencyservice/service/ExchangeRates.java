package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;

import java.util.ArrayList;
import java.util.Optional;

public class ExchangeRates {
    private final ExchangeRatesDAO exchangeRatesDAO;

    public ExchangeRates(ExchangeRatesDAO database) {
        this.exchangeRatesDAO = database;
    }

    public ArrayList<ExchangeRateFull> getExchangeRates() {
        return exchangeRatesDAO.getExchangeRates();
    }

    public Optional<ExchangeRateFull> getExchangeRate(String pair) {
        return exchangeRatesDAO.getExchangeRate(pair);
    }

    public Optional<ExchangeRateFull> addExchangeRate(ExchangeListForm data) {
        return  exchangeRatesDAO.addRate(data.baseCode, data.targetCode, data.rate);
    }

    public Optional<ExchangeRateFull> updateExchangeRate(String pair, ExchangeRateForm data) {
        return exchangeRatesDAO.patchRate(pair, data.rate);
    }

}
