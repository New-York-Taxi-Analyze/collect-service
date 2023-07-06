package com.newyorktaxi.kafka.impl;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.mapper.FailureMessageMapper;
import com.newyorktaxi.repository.CustomFailureMessageRepository;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.kafka.KafkaProducer;
import io.r2dbc.postgresql.codec.Json;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerImpl implements KafkaProducer {

    ReactiveKafkaProducerTemplate<String, TaxiMessage> kafkaTemplate;
    FailureMessageRepository failureMessageRepository;
    CustomFailureMessageRepository customFailureMessageRepository;
    FailureMessageMapper mapper;

    @Override
    public void send(String topicName, UUID key, TaxiMessage message) {
        log.info("Sending message: {} to topic: {}", message, topicName);
        final ProducerRecord<String, TaxiMessage> record = new ProducerRecord<>(topicName, key.toString(), message);
        kafkaTemplate.send(record)
                .doOnSuccess(sendResult -> log.info("Message sent to topic: {}; partition: {}; offset: {}",
                        sendResult.recordMetadata().topic(),
                        sendResult.recordMetadata().partition(),
                        sendResult.recordMetadata().offset()))
                .publishOn(Schedulers.boundedElastic())
                .doOnError(throwable -> {
                    log.error("Error to send message: {} to topic: {} with exception: {}",
                            message, topicName, throwable.getMessage(), throwable);
                    final FailureMessage failureMessage = mapper.toFailureMessage(topicName, key, Json.of(message.toString()));
                    failureMessageRepository.findById(key)
                            .switchIfEmpty(customFailureMessageRepository.insert(failureMessage))
                            .doOnSuccess(aVoid -> log.info("Message saved to retry table: {}", failureMessage))
                            .subscribe();
                })
                .subscribe();
    }
}
