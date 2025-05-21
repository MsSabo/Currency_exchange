package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;

import java.util.List;

public interface Database {
    Currency getCurrency(String code);

    List<Currency> getCurrencies();

    Currency addCurrency(Currency currency);
}
