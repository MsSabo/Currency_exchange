package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateInfo;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;

import java.util.ArrayList;

public class ExchangeRates {
    private final ExchangeRatesDAO exchangeRatesDAO;

    public ExchangeRates(ExchangeRatesDAO database) {
        this.exchangeRatesDAO = database;
    }

    public ArrayList<ExchangeRateInfo> getExchangeRates() {
        System.out.println("getExchangeRates:");

        return exchangeRatesDAO.getExchangeRates();
    }

    public ExchangeRateInfo getExchangeRate(String pair) {
        System.out.println("getExchangeRate: " + pair);
        return exchangeRatesDAO.getExchangeRate(pair);
    }

    public ExchangeRateInfo addExchangeRate(ExchangeListForm data) {
        System.out.println("addExchangeRate: " + data.toString());
        return  exchangeRatesDAO.addRate(data.baseCode, data.targetCode, data.rate);
    }

    public ExchangeRateInfo updateExchangeRate(String pair, ExchangeRateForm data) {
        System.out.println("updateExchangeRate: " + data.toString());
        return exchangeRatesDAO.patchRate(pair, data.rate);
    }

}
