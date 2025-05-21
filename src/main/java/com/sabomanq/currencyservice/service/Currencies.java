package com.sabomanq.currencyservice.service;

import com.sabomanq.currencyservice.dao.Database;
import com.sabomanq.currencyservice.mapper.ViewMapper;
import com.sabomanq.currencyservice.mapper.ViewMapperImpl;
import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.entity.Currency;
import com.sabomanq.currencyservice.model.form.CurrencyForm;

import java.util.List;
import java.util.stream.Collectors;

public class Currencies {
    private final Database database;
    private final ViewMapper mapper = new ViewMapperImpl();

    public Currencies(Database database) {
        this.database = database;
    }

    public CurrencyDTO getCurrency(String code) {
        synchronized (database) {
            Currency result = database.getCurrency(code);
            return new CurrencyDTO(result.id, result.code, result.name, result.sign);
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
