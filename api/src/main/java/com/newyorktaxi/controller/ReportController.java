package com.newyorktaxi.controller;


import com.newyorktaxi.mapper.DatePeriodParamsMapper;
import com.newyorktaxi.mapper.TotalResponseMapper;
import com.newyorktaxi.mapper.TripInfoParamsMapper;
import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import com.newyorktaxi.usecase.params.TripInfoParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    DatePeriodParamsMapper datePeriodParamsMapper;
    TripInfoParamsMapper tripInfoParamsMapper;
    TotalResponseMapper totalResponseMapper;
    MonoUseCase<DatePeriodParams, Total> getTotalUseCase;
    MonoUseCase<TripInfoParams, Void> messageUseCase;

    public Mono<ServerResponse> message(ServerRequest request) {
        return request.bodyToMono(TripInfoRequest.class)
                .doOnNext(tripInfoRequest -> log.info("Sending message: {}", tripInfoRequest))
                .map(tripInfoParamsMapper::toTripInfoParams)
                .flatMap(messageUseCase::execute)
                .then(Mono.defer(() -> {
                    log.debug("Message sent successfully");
                    return ServerResponse.ok()
                            .build();
                }));
    }

    public Mono<ServerResponse> total(ServerRequest request) {
        final int year = Integer.parseInt(request.queryParam("year").orElseThrow());
        final int month = Integer.parseInt(request.queryParam("month").orElseThrow());
        final int day = Integer.parseInt(request.queryParam("day").orElseThrow());

        return Mono.just(datePeriodParamsMapper.toDatePeriodParams(year, month, day))
                .flatMap(getTotalUseCase::execute)
                .map(totalResponseMapper::toTotalResponse)                .flatMap(totalResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(totalResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
