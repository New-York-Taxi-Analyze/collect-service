package com.newyorktaxi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.newyorktaxi.TestData;
import com.newyorktaxi.mapper.TotalMapper;
import com.newyorktaxi.model.DatePeriod;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.impl.GetTotalUseCase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerTest {

    @Mock
    TotalMapper totalMapper;
    @Mock
    GetTotalUseCase getTotalUseCase;

    @InjectMocks
    ReportController reportController;

    @Test
    @DisplayName("Should successfully return total response")
    void getTotal() {
        final TotalResponse expected = TestData.buildTotalResponse();
        final DatePeriod totalRequest = TestData.buildDatePeriod();

        when(totalMapper.toDatePeriod(TestData.YEAR, TestData.MONTH, TestData.DAY)).thenReturn(totalRequest);
        when(getTotalUseCase.execute(totalRequest)).thenReturn(expected);

        final TotalResponse actual = reportController.getTotal(TestData.YEAR, TestData.MONTH, TestData.DAY);

        assertThat(actual)
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
