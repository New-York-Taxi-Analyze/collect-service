package com.newyorktaxi.controller;

import com.newyorktaxi.mapper.DatePeriodParamsMapper;
import com.newyorktaxi.mapper.TotalResponseMapper;
import com.newyorktaxi.mapper.TripInfoParamsMapper;
import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.usecase.FunctionalUseCase;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import com.newyorktaxi.usecase.params.TripInfoParams;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    DatePeriodParamsMapper datePeriodParamsMapper;
    TripInfoParamsMapper tripInfoParamsMapper;
    TotalResponseMapper totalResponseMapper;
    FunctionalUseCase<DatePeriodParams, Total> getTotalUseCase;
    FunctionalUseCase<TripInfoParams, Void> messageUseCase;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void message(@RequestBody TripInfoRequest messageRequest) {
        log.info("Sending message: {}", messageRequest);
        final TripInfoParams tripInfoParams = tripInfoParamsMapper.toTripInfoParams(messageRequest);

        messageUseCase.execute(tripInfoParams);
        log.debug("Message sent successfully with data: {}", tripInfoParams);
    }

    @GetMapping(value = "/total")
    public TotalResponse getTotal(@RequestParam @Positive Integer year,
                                  @RequestParam(required = false) @Min(1) @Max(12) Integer month,
                                  @RequestParam(required = false) @Min(1) @Max(31) Integer day) {
        log.info("Getting total count with params: year={}, month={}, day={}", year, month, day);

        final DatePeriodParams datePeriodParams = datePeriodParamsMapper.toDatePeriodParams(year, month, day);
        final Total total = getTotalUseCase.execute(datePeriodParams);
        final TotalResponse totalResponse = totalResponseMapper.toTotalResponse(total);

        log.debug("Total count retrieved successfully with data: {}", totalResponse);
        return totalResponse;
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Hello World";
    }
}
