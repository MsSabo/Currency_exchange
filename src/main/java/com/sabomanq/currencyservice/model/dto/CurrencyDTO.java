package com.sabomanq.currencyservice.model.dto;

public class CurrencyDTO {
    public int id;
    public String code;
    public String name;
    public String sign;

    public CurrencyDTO(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyDTO(String code, String name, String sign) {
        this.id = -1;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyDTO(CurrencyDTO currencyDTO) {
        this.id = currencyDTO.id;
        this.code = currencyDTO.code;
        this.name = currencyDTO.name;
        this.sign = currencyDTO.sign;
    }
}
