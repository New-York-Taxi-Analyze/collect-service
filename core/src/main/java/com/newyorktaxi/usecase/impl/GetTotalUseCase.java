package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.model.DatePeriod;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.FunctionalUseCase;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetTotalUseCase implements FunctionalUseCase<DatePeriod, TotalResponse> {

    @Override
    public TotalResponse execute(@Valid DatePeriod totalRequest) {
        return TotalResponse.builder()
                .total(201.)
                .date(totalRequest.getYear() + "/" + totalRequest.getMonth() + "/" + totalRequest.getDay())
                .build();
    }
}
