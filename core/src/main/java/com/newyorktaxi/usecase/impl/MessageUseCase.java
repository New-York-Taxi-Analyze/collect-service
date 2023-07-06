package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.mapper.TaxiMessageMapper;
import com.newyorktaxi.kafka.KafkaProducer;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.TripInfoParams;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageUseCase implements MonoUseCase<TripInfoParams, Void> {

    @Value("${kafka.topic.name}")
    @NonFinal
    String topicName;

    KafkaProducer kafkaProducer;
    TaxiMessageMapper taxiMessageMapper;

    @Override
    public Mono<Void> execute(TripInfoParams params) {
        return Mono.just(params)
                .map(taxiMessageMapper::toTaxiMessage)
                .flatMap(taxiMessage -> {
                    kafkaProducer.send(topicName, UUID.randomUUID(), taxiMessage);
                    return Mono.empty();
                });
    }
}
