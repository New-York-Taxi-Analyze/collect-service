package com.newyorktaxi.service.impl;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.mapper.FailureMessageMapper;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.service.KafkaProducer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.BiConsumer;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    KafkaTemplate<K, V> kafkaTemplate;
    FailureMessageRepository failureMessageRepository;
    FailureMessageMapper mapper;

    @Override
    public void send(String topicName, K key, V message) {
        log.info("Sending message: {} to topic: {}", message, topicName);

        try {
            kafkaTemplate.send(topicName, key, message)
                    .whenComplete(callback());
        } catch (KafkaException e) {
            log.error("Error to send message: {} to topic: {} with exception: {}", message, topicName, e.getMessage(), e);
            if (key instanceof String keyId && message instanceof TaxiMessage data) {
                final FailureMessage failureMessage = mapper.toFailureMessage(topicName, UUID.fromString(keyId), data);
                if (!failureMessageRepository.existsById(failureMessage.getKey())) {
                    failureMessageRepository.save(failureMessage);
                    log.info("Message saved to retry table: {}", failureMessage);
                }
            }
            throw e;
        }
    }

    private BiConsumer<SendResult<K, V>, Throwable> callback() {
        return (input, exception) -> {
            if (exception != null) {
                log.error("Error while sending message: {}", exception.getMessage(), exception);
            } else {
                final RecordMetadata metadata = input.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}",
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
            }
        };
    }
}
