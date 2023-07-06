package com.newyorktaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newyorktaxi.TestData;
import com.newyorktaxi.configuration.RouterConfig;
import com.newyorktaxi.mapper.DatePeriodParamsMapper;
import com.newyorktaxi.mapper.TotalResponseMapper;
import com.newyorktaxi.mapper.TripInfoParamsMapper;
import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.usecase.impl.GetTotalUseCase;
import com.newyorktaxi.usecase.impl.MessageUseCase;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import com.newyorktaxi.usecase.params.TripInfoParams;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@Tag("unit")
@WebFluxTest
@ContextConfiguration(classes = {RouterConfig.class, ReportController.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    DatePeriodParamsMapper totalMapper;
    @MockBean
    TripInfoParamsMapper tripInfoParamsMapper;
    @MockBean
    TotalResponseMapper totalResponseMapper;
    @MockBean
    GetTotalUseCase getTotalUseCase;
    @MockBean
    MessageUseCase messageUseCase;
    @MockBean
    UserController userController;

    @Test
    @DisplayName("Should successfully return HttpStatus.OK for the message response")
    @SneakyThrows
    void testMessage() {
        final TripInfoRequest tripInfoRequest = TestData.buildTripInfoRequest();
        final TripInfoParams tripInfoParams = TestData.buildTripInfoParams();
        final String json = objectMapper.writeValueAsString(tripInfoRequest);
        when(tripInfoParamsMapper.toTripInfoParams(tripInfoRequest)).thenReturn(tripInfoParams);
        doReturn(Mono.empty()).when(messageUseCase).execute(tripInfoParams);

        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser())
                .post()
                .uri("/api/v1/message")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Should successfully return total response")
    @SneakyThrows
    void testGetTotal() {
        final TotalResponse expected = TestData.buildTotalResponse();
        final DatePeriodParams totalRequest = TestData.buildDatePeriod();
        final Total total = TestData.buildTotal();
        final Mono<Total> totalMono = Mono.just(total);

        when(totalMapper.toDatePeriodParams(TestData.YEAR, TestData.MONTH, TestData.DAY)).thenReturn(totalRequest);
        doReturn(totalMono).when(getTotalUseCase).execute(totalRequest);
        when(totalResponseMapper.toTotalResponse(total)).thenReturn(expected);

        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/total")
                        .queryParam("year", TestData.YEAR.toString())
                        .queryParam("month", TestData.MONTH.toString())
                        .queryParam("day", TestData.DAY.toString())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TotalResponse.class).isEqualTo(expected);
    }
}
