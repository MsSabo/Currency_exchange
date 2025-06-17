package com.sabomanq.currencyservice.model.mapper;

import com.sabomanq.currencyservice.model.dto.ExchangeRateDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateDTO toDto(ExchangeRateFull exchangeRateFull);
}
