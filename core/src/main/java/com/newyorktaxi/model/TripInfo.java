package com.newyorktaxi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TripInfo {
    Integer vendorId;
    Instant tpepPickupDatetime;
    Instant tpepDropoffDatetime;
    Integer passengerCount;
    Float tripDistance;
    RateCodeType rateCodeId;
    StoreAndFwdFlagType storeAndFwdFlag;
    Integer puLocationId;
    Integer doLocationId;
    PaymentType paymentType;
    Double fareAmount;
    Double extra;
    Double mtaTax;
    Double tipAmount;
    Double tollsAmount;
    Double improvementSurcharge;
    Double totalAmount;
}
