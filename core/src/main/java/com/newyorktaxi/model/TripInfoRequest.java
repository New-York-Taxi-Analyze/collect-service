package com.newyorktaxi.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TripInfoRequest {
    Short vendorId;
    Instant tpepPickupDatetime;
    Instant tpepDropoffDatetime;
    Integer passengerCount;
    Double tripDistance;
    RateCodeType rateCodeId;
    StoreAndFwdFlagType storeAndFwdFlag;
    Integer puLocationId;
    Integer doLocationId;
    PaymentType paymentType;
    BigDecimal fareAmount;
    BigDecimal extra;
    BigDecimal mtaTax;
    BigDecimal tipAmount;
    BigDecimal tollsAmount;
    BigDecimal improvementSurcharge;
    BigDecimal totalAmount;
}
