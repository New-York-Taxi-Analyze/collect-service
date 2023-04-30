package com.newyorktaxi.mapper;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.model.PaymentType;
import com.newyorktaxi.model.RateCodeType;
import com.newyorktaxi.model.TripInfo;
import org.mapstruct.Mapper;

@Mapper
public interface TaxiMessageMapper {

    TaxiMessage toTaxiMessage(TripInfo source);

    default Integer mapPaymentType(PaymentType source) {
        return source == null ? null : source.getValue();
    }

    default Integer mapRateCodeType(RateCodeType source) {
        return source == null ? null : source.getValue();
    }
}
