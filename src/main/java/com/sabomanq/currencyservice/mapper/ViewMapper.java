package com.sabomanq.currencyservice.mapper;

import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.form.CurrencyForm;

public interface ViewMapper {
    public CurrencyDTO toDto(CurrencyForm currency);
    public CurrencyForm toForm(CurrencyDTO currency);
}
