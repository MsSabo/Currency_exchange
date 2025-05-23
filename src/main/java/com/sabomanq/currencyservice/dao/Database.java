package com.sabomanq.currencyservice.dao;

import com.sabomanq.currencyservice.model.entity.Currency;

import java.util.List;

public interface Database {
    Currency getCurrency(String code) throws DatabaseError, NotFoundException;

    List<Currency> getCurrencies() throws DatabaseError;

    Currency addCurrency(Currency currency) throws UniqueConstraintViolationException;
}
