package com.newyorktaxi;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.entity.StatusEnum;
import io.r2dbc.postgresql.codec.Json;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TestData {

    public FailureMessage buildFailureMessage() {
        return FailureMessage.builder()
                .key(UUID.randomUUID())
                .topic("taxi-messages")
                .message(Json.of(buildTaxiMessage().toString()))
                .status(StatusEnum.RETRY)
                .build();
    }

    private TaxiMessage buildTaxiMessage() {
        return TaxiMessage.newBuilder()
                .setVendorId(1)
                .build();
    }
}
