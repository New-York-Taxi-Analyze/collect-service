package com.newyorktaxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newyorktaxi.TestData;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

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

    @Test
    @WithMockUser
    @DisplayName("Should successfully return HttpStatus.OK for the message response")
    @SneakyThrows
    void testMessage() {
        final TripInfoRequest tripInfoRequest = TestData.buildTripInfoRequest();
        final TripInfoParams tripInfoParams = TestData.buildTripInfoParams();
        final String json = objectMapper.writeValueAsString(tripInfoRequest);

        when(tripInfoParamsMapper.toTripInfoParams(tripInfoRequest)).thenReturn(tripInfoParams);
        doNothing().when(messageUseCase).execute(tripInfoParams);

        mockMvc.perform(post("/api/v1/message")
                        .with(csrf())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should successfully return total response")
    @SneakyThrows
    void testGetTotal() {
        final TotalResponse expected = TestData.buildTotalResponse();
        final DatePeriodParams totalRequest = TestData.buildDatePeriod();
        final Total total = TestData.buildTotal();

        when(totalMapper.toDatePeriodParams(TestData.YEAR, TestData.MONTH, TestData.DAY)).thenReturn(totalRequest);
        when(getTotalUseCase.execute(totalRequest)).thenReturn(total);
        when(totalResponseMapper.toTotalResponse(total)).thenReturn(expected);

        mockMvc.perform(get("/api/v1/total")
                        .param("year", TestData.YEAR.toString())
                        .param("month", TestData.MONTH.toString())
                        .param("day", TestData.DAY.toString()))
                .andExpect(status().isOk());
    }
}
