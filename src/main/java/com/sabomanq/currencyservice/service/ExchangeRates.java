package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;

import java.util.ArrayList;
import java.util.Optional;

public class ExchangeRates {
    private final ExchangeRatesDAO exchangeRatesDAO;

    public ExchangeRates(ExchangeRatesDAO database) {
        this.exchangeRatesDAO = database;
    }

    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRatesDAO.getExchangeRates();
    }

    public Optional<ExchangeRate> getExchangeRate(String pair) {
        return exchangeRatesDAO.getExchangeRate(pair);
    }

    public ExchangeRate addExchangeRate(ExchangeListForm data) {
        return exchangeRatesDAO.addRate(data.baseCode, data.targetCode, data.rate);
    }

    public ExchangeRate updateExchangeRate(String pair, ExchangeRateForm data) throws NotFoundException {
        return exchangeRatesDAO.patchRate(pair, data.rate);
    }

}
