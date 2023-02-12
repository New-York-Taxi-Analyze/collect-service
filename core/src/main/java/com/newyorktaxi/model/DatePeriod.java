package com.newyorktaxi.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DatePeriod {

    Integer year;
    Integer month;
    Integer day;
}
