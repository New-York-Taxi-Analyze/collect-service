package com.newyorktaxi.model;

import com.newyorktaxi.validation.DatePeriodConstraint;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@DatePeriodConstraint
public class DatePeriod {

    Integer year;
    Integer month;
    Integer day;
}
