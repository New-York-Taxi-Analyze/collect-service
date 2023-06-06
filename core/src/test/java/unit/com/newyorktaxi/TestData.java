package com.newyorktaxi;

import com.newyorktaxi.model.PaymentType;
import com.newyorktaxi.model.RateCodeType;
import com.newyorktaxi.model.StoreAndFwdFlagType;
import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.model.UserRequest;
import com.newyorktaxi.usecase.params.DatePeriodParams;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class TestData {

    public static final Integer YEAR = 2022;
    public static final Integer MONTH = 12;
    public static final Integer DAY = 31;
    public static final Double TOTAL = 999.;
    public static final String USER_EMAIL = "test@test.test";
    public static final String USER_PASSWORD = "12345";

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

    public TripInfoRequest buildTripInfoRequest() {
        short vendorId = 1;
        return TripInfoRequest.builder()
                .vendorId(vendorId)
                .tpepPickupDatetime(Instant.now())
                .tpepDropoffDatetime(Instant.now())
                .passengerCount(1)
                .tripDistance(1.0)
                .rateCodeId(RateCodeType.NEWARK)
                .storeAndFwdFlag(StoreAndFwdFlagType.N)
                .puLocationId(1)
                .doLocationId(1)
                .paymentType(PaymentType.CASH)
                .fareAmount(BigDecimal.TEN)
                .extra(BigDecimal.TEN)
                .mtaTax(BigDecimal.TEN)
                .tipAmount(BigDecimal.TEN)
                .tollsAmount(BigDecimal.TEN)
                .improvementSurcharge(BigDecimal.TEN)
                .totalAmount(BigDecimal.TEN)
                .build();
    }

    public UserRequest buildUserRequest(String email, String password) {
        return UserRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
