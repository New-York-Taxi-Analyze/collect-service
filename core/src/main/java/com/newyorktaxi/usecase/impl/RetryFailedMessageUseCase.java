package com.newyorktaxi.usecase.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.StatusEnum;
import com.newyorktaxi.kafka.KafkaProducer;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.usecase.FunctionalUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RetryFailedMessageUseCase implements FunctionalUseCase<Void, Void> {

    FailureMessageRepository repository;
    KafkaProducer kafkaProducer;
    ObjectMapper objectMapper;

    @Override
    public Void execute(Void params) {
        repository.findAllByStatus(StatusEnum.RETRY)
                .collectList()
                .doOnNext(allByStatus -> {
                    if (!allByStatus.isEmpty()) {
                        log.info("Retrying {} messages", allByStatus.size());
                    }
                })
                .flatMapMany(Flux::fromIterable)
                .doOnNext(failureMessage -> {
                    try {
                        final TaxiMessage taxiMessage = objectMapper.readValue(failureMessage.getMessage().asArray(), TaxiMessage.class);
                        kafkaProducer.send(failureMessage.getTopic(), failureMessage.getKey(), taxiMessage);
                    } catch (JsonProcessingException e ) {
                        log.error("Error parsing JSON to TaxiMessage", e);
                    } catch (IOException e) {
                        log.error("Error sending message to Kafka", e);
                    }
                })
                .doOnNext(failureMessage -> failureMessage.setStatus(StatusEnum.SUCCESS))
                .flatMap(repository::save)
                .doOnNext(failureMessage -> log.info("Message successfully retried: {}", failureMessage))
                .subscribe();
        return null;
    }
}
