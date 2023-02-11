package com.newyorktaxi.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.newyorktaxi.TestData;
import com.newyorktaxi.model.DatePeriod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class TotalMapperTest {

    private static final TotalMapper mapper = new TotalMapperImpl();

    @Test
    @DisplayName("Should successfully map request data to date period")
    void testToTotalRequest() {
        final DatePeriod expected = TestData.buildDatePeriod();

        final DatePeriod actual = mapper.toDatePeriod(TestData.YEAR, TestData.MONTH, TestData.DAY);

        assertThat(actual)
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
