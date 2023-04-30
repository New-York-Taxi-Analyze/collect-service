package com.newyorktaxi.controller;

import com.newyorktaxi.mapper.TotalMapper;
import com.newyorktaxi.mapper.TripInfoMapper;
import com.newyorktaxi.model.DatePeriod;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.model.TripInfo;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.usecase.FunctionalUseCase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    TotalMapper totalMapper;
    TripInfoMapper tripInfoMapper;
    FunctionalUseCase<DatePeriod, TotalResponse> getTotalUseCase;
    FunctionalUseCase<TripInfo, Void> messageUseCase;

    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> message(@RequestBody TripInfoRequest messageRequest) {
        log.info("Sending message: {}", messageRequest);
        final TripInfo message = tripInfoMapper.toTripInfo(messageRequest);

        messageUseCase.execute(message);
        log.debug("Message sent successfully with data: {}", message);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/total")
    public TotalResponse getTotal(@RequestParam @Positive Integer year,
                                  @RequestParam(required = false) @Min(1) @Max(12) Integer month,
                                  @RequestParam(required = false) @Min(1) @Max(31) Integer day) {
        log.info("Getting total count with params: year={}, month={}, day={}", year, month, day);

        final DatePeriod totalRequest = totalMapper.toDatePeriod(year, month, day);
        final TotalResponse totalResponse = getTotalUseCase.execute(totalRequest);

        log.debug("Total count retrieved successfully with data: {}", totalResponse);
        return totalResponse;
    }
}
