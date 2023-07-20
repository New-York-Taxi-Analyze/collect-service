package com.newyorktaxi.controller;

import com.newyorktaxi.CollectServiceApplication;
import com.newyorktaxi.TestData;
import com.newyorktaxi.model.TripInfoRequest;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {CollectServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"taxi-messages"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReportControllerIntegrationTest {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;

    @Autowired
    WebClient webClient;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    Consumer<String, Object> consumer;

    @BeforeEach
    void setUp() {
        final Map<String, Object> configs = new HashMap<>(
                KafkaTestUtils.consumerProps("group_test", "true", embeddedKafkaBroker));
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put("schema.registry.url", "http://localhost:8081");
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new KafkaAvroDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    @DisplayName("Should send message to kafka topic")
    @Timeout(10)
    void testMessage() {
        final TripInfoRequest tripInfoRequest = TestData.buildTripInfoRequest();
        final String token = authToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        final HttpEntity<TripInfoRequest> request = new HttpEntity<>(tripInfoRequest, headers);
        final ResponseEntity<String> exchange = restTemplate.exchange("/api/v1/message", HttpMethod.POST, request,
                String.class);

        final ConsumerRecord<String, Object> singleRecord = KafkaTestUtils.getSingleRecord(consumer, "taxi-messages");

        assertThat(singleRecord).isNotNull();
        assertThat(exchange.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @SneakyThrows
    private String authToken() {
        final URI authorizationURI = new URIBuilder(jwkSetUri).build();
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("client_id", Collections.singletonList("newyorktaxiclient"));
        formData.put("client_secret", Collections.singletonList("d1JbuRKG2iS0tveThqFinwO1cMAGt1xm"));
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("username", Collections.singletonList("user@test.com"));
        formData.put("password", Collections.singletonList("12345"));

        final String result = webClient.post()
                .uri(authorizationURI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        final JacksonJsonParser jsonParser = new JacksonJsonParser();

        final String accessToken = jsonParser.parseMap(result)
                .get("access_token")
                .toString();
        log.info("Access token: {}", accessToken);
        return accessToken;
    }
}

