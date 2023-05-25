package com.newyorktaxi.usecase.params;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
//@DatePeriodConstraint
public class DatePeriodParams {

    Integer year;
    Integer month;
    Integer day;
}
