package com.sabomanq.currencyservice.model.entity;

public class Currency {
    /// Constant names for table fields
    public int id;
    public String code;
    public String name;
    public String sign;

    public Currency(int id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public Currency(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
