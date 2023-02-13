package com.newyorktaxi.service.impl;

import com.newyorktaxi.service.KafkaProducer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message) {
        log.info("Sending message: {} to topic: {}", message, topicName);

        try {
            kafkaTemplate.send(topicName, key, message);
        } catch (KafkaException e) {
            log.error("Error to send message: {} to topic: {} with exception: {}",
                    message, topicName, e.getMessage(), e);
            throw e;
        }
    }
}
