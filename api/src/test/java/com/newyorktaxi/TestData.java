package com.newyorktaxi;

import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static final Integer YEAR = 2022;
    public static final Integer MONTH = 12;
    public static final Integer DAY = 31;
    public static final Double TOTAL = 999.;

    public TotalResponse buildTotalResponse() {
        return TotalResponse.builder()
                .total(TOTAL)
                .date(YEAR + "/" + MONTH + "/" + DAY)
                .build();
    }

    public Total buildTotal() {
        return Total.builder()
                .total(TOTAL)
                .date(YEAR + "/" + MONTH + "/" + DAY)
                .build();
    }

    public DatePeriodParams buildDatePeriod() {
        return DatePeriodParams.builder()
                .year(YEAR)
                .month(MONTH)
                .day(DAY)
                .build();
    }
}
