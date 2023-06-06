package com.newyorktaxi.controller;

import com.newyorktaxi.TestData;
import com.newyorktaxi.mapper.DatePeriodParamsMapper;
import com.newyorktaxi.mapper.TotalResponseMapper;
import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.impl.GetTotalUseCase;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerTest {

    @Mock
    DatePeriodParamsMapper totalMapper;
    @Mock
    TotalResponseMapper totalResponseMapper;
    @Mock
    GetTotalUseCase getTotalUseCase;

    @InjectMocks
    ReportController reportController;

    @Test
    @DisplayName("Should successfully return total response")
    void getTotal() {
        final TotalResponse expected = TestData.buildTotalResponse();
        final DatePeriodParams totalRequest = TestData.buildDatePeriod();
        final Total total = TestData.buildTotal();

        when(totalMapper.toDatePeriodParams(TestData.YEAR, TestData.MONTH, TestData.DAY)).thenReturn(totalRequest);
        when(getTotalUseCase.execute(totalRequest)).thenReturn(total);
        when(totalResponseMapper.toTotalResponse(total)).thenReturn(expected);

        final TotalResponse actual = reportController.getTotal(TestData.YEAR, TestData.MONTH, TestData.DAY);

        assertThat(actual)
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
