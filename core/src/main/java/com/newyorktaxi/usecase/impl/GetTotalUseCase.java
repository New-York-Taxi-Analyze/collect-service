package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.model.Total;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import com.newyorktaxi.usecase.FunctionalUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
//@Validated
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetTotalUseCase implements FunctionalUseCase<DatePeriodParams, Total> {

    @Override
    public Total execute(/*@Valid*/ DatePeriodParams params) {
        return Total.builder()
                .total(201.)
                .date(params.getYear() + "/" + params.getMonth() + "/" + params.getDay())
                .build();
    }
}
