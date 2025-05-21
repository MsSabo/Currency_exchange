package com.sabomanq.currencyservice.model.form;

///  for post rate
public class ExchangeListForm extends ExchangeRateForm {
    public String baseCode;
    public String targetCode;

    public ExchangeListForm(String baseCode, String targetCode, float rate) {
        super(rate);
        this.baseCode = baseCode;
        this.targetCode = targetCode;
    }
}
