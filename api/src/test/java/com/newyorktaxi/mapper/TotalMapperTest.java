package com.newyorktaxi.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.newyorktaxi.TestData;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class TotalMapperTest {

    private static final DatePeriodParamsMapper mapper = new DatePeriodParamsMapperImpl();

    @Test
    @DisplayName("Should successfully map request data to date period")
    void testToTotalRequest() {
        final DatePeriodParams expected = TestData.buildDatePeriod();

        final DatePeriodParams actual = mapper.toDatePeriodParams(TestData.YEAR, TestData.MONTH, TestData.DAY);

        assertThat(actual)
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
