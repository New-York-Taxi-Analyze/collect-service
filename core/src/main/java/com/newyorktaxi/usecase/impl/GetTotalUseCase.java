package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.model.Total;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetTotalUseCase implements MonoUseCase<DatePeriodParams, Total> {

    @Override
    public Mono<Total> execute(DatePeriodParams params) {
        return Mono.just(params)
                .map(datePeriodParams -> Total.builder()
                        .total(201.)
                        .date(params.getYear() + "/" + params.getMonth() + "/" + params.getDay())
                        .build());
    }
}
