package com.newyorktaxi.controller;

import com.newyorktaxi.CollectServiceApplication;
import com.newyorktaxi.TestData;
import com.newyorktaxi.model.TripInfoRequest;
import com.newyorktaxi.model.UserRequest;
import com.newyorktaxi.repository.UserRepository;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CollectServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"taxi-messages"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    Consumer<String, Object> consumer;

    @BeforeEach
    void setUp() {
        final UserRequest userRequest = TestData.buildUserRequest(TestData.USER_EMAIL, TestData.USER_PASSWORD);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);
        final ResponseEntity<String> exchange = restTemplate.exchange("/api/v1/createUser", HttpMethod.POST, request,
                String.class);

        assertThat(exchange.getStatusCode().is2xxSuccessful()).isTrue();

        final Map<String, Object> configs = new HashMap<>(
            KafkaTestUtils.consumerProps("group_test", "true", embeddedKafkaBroker));
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put("schema.registry.url", "http://localhost:8081");
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new KafkaAvroDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        userRepository.findByEmail(TestData.USER_EMAIL).ifPresent(userRepository::delete);
        consumer.close();
    }

    @Test
    @Timeout(5)
    void testMessage() {
        final TripInfoRequest tripInfoRequest = TestData.buildTripInfoRequest();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(TestData.USER_EMAIL, TestData.USER_PASSWORD);

        final HttpEntity<TripInfoRequest> request = new HttpEntity<>(tripInfoRequest, headers);
        final ResponseEntity<String> exchange = restTemplate.exchange("/api/v1/message", HttpMethod.POST, request,
                String.class);

        final ConsumerRecord<String, Object> singleRecord = KafkaTestUtils.getSingleRecord(consumer, "taxi-messages");

        assertThat(singleRecord).isNotNull();
        assertThat(exchange.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
