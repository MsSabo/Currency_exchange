package com.sabomanq.currencyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sabomanq.currencyservice.model.entity.Currency;

@JsonPropertyOrder({ "id", "name", "code", "sign"})
public class CurrencyDTO {
    public int id;
    public String code;
    public String name;
    public String sign;

    public CurrencyDTO(Currency currency) {
        this.id = currency.id;
        this.code = currency.code;
        this.name = currency.name;
        this.sign = currency.sign;
    }

    public CurrencyDTO(int id, String code, String name, String sign) {
        this.id  = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
