package com.newyorktaxi.controller;

import com.newyorktaxi.mapper.TotalMapper;
import com.newyorktaxi.model.DatePeriod;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.FunctionalUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    TotalMapper totalMapper;
    FunctionalUseCase<DatePeriod, TotalResponse> getTotalUseCase;

    @PostMapping("/message")
    public String message() {
        return "test";
    }

    @GetMapping(value = "/total", produces = "application/json")
    public TotalResponse getTotal(@RequestParam @Positive Integer year,
                                  @RequestParam(required = false) @Min(1) @Max(12) Integer month,
                                  @RequestParam(required = false) @Min(1) @Max(31) Integer day) {
        log.info("Getting total count with params: year={}, month={}, day={}", year, month, day);

        final DatePeriod totalRequest = totalMapper.toDatePeriod(year, month, day);

        return getTotalUseCase.execute(totalRequest);
    }
}
