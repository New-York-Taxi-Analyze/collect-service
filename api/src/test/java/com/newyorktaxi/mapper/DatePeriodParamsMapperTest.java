package com.newyorktaxi.mapper;

import com.newyorktaxi.TestData;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("unit")
@SpringBootTest(classes = {DatePeriodParamsMapperImpl.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
class DatePeriodParamsMapperTest {

    @Autowired
    DatePeriodParamsMapper mapper;

    @Test
    @DisplayName("Should successfully map request data to date period")
    void testToTotalRequest() {
        final DatePeriodParams expected = TestData.buildDatePeriod();

        final DatePeriodParams actual = mapper.toDatePeriodParams(TestData.YEAR, TestData.MONTH, TestData.DAY);

        Assertions.assertThat(actual)
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
