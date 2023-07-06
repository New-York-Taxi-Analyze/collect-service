package com.newyorktaxi.kafka;

import com.newyorktaxi.avro.model.TaxiMessage;

import java.util.UUID;

public interface KafkaProducer {

    void send(String topicName, UUID key, TaxiMessage message);

}
