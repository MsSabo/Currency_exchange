package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.Database;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.mapper.CurrencyMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Currencies {
    private final Database database;

    public Currencies(Database database) {
        this.database = database;
    }

    public CurrencyDTO getCurrency(String code) throws NotFoundException {
        synchronized (database) {
            System.out.println("getCurrency");
            Currency result = database.getCurrency(code);
            return CurrencyMapper.INSTANCE.currencyToDto(result);
        }
    }
    
    public List<CurrencyDTO> getAllCurrencies() {
        synchronized (database) {
           return database.getCurrencies().stream().map(e -> new CurrencyDTO(e.id, e.code, e.name, e.sign)).collect(Collectors.toList());
        }
    }

    public CurrencyDTO add(CurrencyForm currency) {
        synchronized (database) {
            Currency result = database.addCurrency(new Currency(currency.code, currency.name, currency.sign));
            return new CurrencyDTO(result.id, result.code, result.name, result.sign);
        }
    }
}
