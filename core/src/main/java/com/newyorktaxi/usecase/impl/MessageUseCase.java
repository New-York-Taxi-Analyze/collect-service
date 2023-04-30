package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.mapper.TaxiMessageMapper;
import com.newyorktaxi.model.TripInfo;
import com.newyorktaxi.service.KafkaProducer;
import com.newyorktaxi.usecase.FunctionalUseCase;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageUseCase implements FunctionalUseCase<TripInfo, Void> {

    KafkaProducer<String, TaxiMessage> kafkaProducer;
    TaxiMessageMapper taxiMessageMapper;

    @Override
    public Void execute(TripInfo param) {
        final TaxiMessage taxiMessage = taxiMessageMapper.toTaxiMessage(param);

        kafkaProducer.send("taxi-messages", UUID.randomUUID().toString(), taxiMessage);

        return null;
    }
}
