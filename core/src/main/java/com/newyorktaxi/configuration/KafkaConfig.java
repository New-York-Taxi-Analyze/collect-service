package com.newyorktaxi.configuration;

import com.newyorktaxi.avro.model.TaxiMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, TaxiMessage> reactiveKafkaProducerTemplate(KafkaProperties properties) {
        return new ReactiveKafkaProducerTemplate<>(
                SenderOptions.create(
                        properties.buildProducerProperties()
                ));
    }
}
