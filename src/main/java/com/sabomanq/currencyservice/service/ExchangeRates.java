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
        System.out.println("getExchangeRates:");
        return exchangeRatesDAO.getExchangeRates();
    }

    public Optional<ExchangeRateFull> getExchangeRate(String pair) {
        System.out.println("getExchangeRate: " + pair);
        return exchangeRatesDAO.getExchangeRate(pair);
    }

    public Optional<ExchangeRateFull> addExchangeRate(ExchangeListForm data) {
        System.out.println("addExchangeRate: " + data.toString());
        return  exchangeRatesDAO.addRate(data.baseCode, data.targetCode, data.rate);
    }

    public Optional<ExchangeRateFull> updateExchangeRate(String pair, ExchangeRateForm data) {
        System.out.println("updateExchangeRate: " + data.toString());
        return exchangeRatesDAO.patchRate(pair, data.rate);
    }

}
