package com.newyorktaxi.scheduler;

import com.newyorktaxi.TestApp;
import com.newyorktaxi.TestData;
import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.StatusEnum;
import com.newyorktaxi.kafka.KafkaProducer;
import com.newyorktaxi.repository.CustomFailureMessageRepository;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.usecase.impl.RetryFailedMessageUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {TestApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "scheduler.retry-failed-message.cron-expression=*/1 * * * * *")
class RetryFailedMessageTest {

    @Autowired
    RetryFailedMessage retryFailedMessage;

    @Autowired
    RetryFailedMessageUseCase retryFailedMessageUseCase;

    @Autowired
    CustomFailureMessageRepository customFailureMessageRepository;

    @Autowired
    FailureMessageRepository repository;

    @MockBean
    KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        customFailureMessageRepository.insert(TestData.buildFailureMessage())
                .subscribe();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void retry() {
        doNothing().when(kafkaProducer).send(anyString(), any(UUID.class),  any(TaxiMessage.class));

        Mono.delay(Duration.ofSeconds(5))
                .thenMany(repository.findAllByStatus(StatusEnum.SUCCESS))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
