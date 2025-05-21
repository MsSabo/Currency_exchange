package com.sabomanq.currencyservice.mapper;

import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.form.CurrencyForm;

public class ViewMapperImpl implements ViewMapper {
    public ViewMapperImpl() {}

    public CurrencyDTO toDto(CurrencyForm form) {
        return new CurrencyDTO(form.code, form.name, form.sign);
    }
    public CurrencyForm toForm(CurrencyDTO dto) {
        return new CurrencyForm(dto.code, dto.name, dto.sign);
    }
}
