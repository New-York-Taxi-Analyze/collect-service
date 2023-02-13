package com.newyorktaxi.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka-producer-config")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaProducerProperties {
    String keySerializerClass;
    String valueSerializerClass;
    String compressionType;
    String acks;
    Integer batchSize;
    Integer batchSizeBoostFactor;
    Integer lingerMs;
    Integer requestTimeoutMs;
    Integer retryCount;
}
