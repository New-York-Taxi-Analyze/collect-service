package com.newyorktaxi.scheduler;

import com.newyorktaxi.CollectServiceApplication;
import com.newyorktaxi.TestData;
import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.StatusEnum;
import com.newyorktaxi.repository.FailureMessageRepository;
import com.newyorktaxi.service.KafkaProducer;
import com.newyorktaxi.usecase.impl.RetryFailedMessageUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {CollectServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "scheduler.retry-failed-message.cron-expression=*/1 * * * * *")
class RetryFailedMessageTest {

    @Autowired
    RetryFailedMessage retryFailedMessage;

    @Autowired
    RetryFailedMessageUseCase retryFailedMessageUseCase;

    @Autowired
    FailureMessageRepository repository;

    @MockBean
    KafkaProducer<String, TaxiMessage> kafkaProducer;

    @BeforeEach
    void setUp() {
        repository.saveAll(List.of(TestData.buildFailureMessage(), TestData.buildFailureMessage()));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void retry() {
        doNothing().when(kafkaProducer).send(anyString(), anyString(),  any(TaxiMessage.class));
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(repository.findAllByStatus(StatusEnum.SUCCESS).size()).isEqualTo(2);
        });
    }
}
