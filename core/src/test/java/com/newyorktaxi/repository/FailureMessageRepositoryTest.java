package com.newyorktaxi.repository;

import com.newyorktaxi.TestData;
import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.entity.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ComponentScan(basePackages = {"com.newyorktaxi.repository"})
class FailureMessageRepositoryTest {

    @Autowired
    FailureMessageRepository repository;

    @Autowired
    CustomFailureMessageRepository customRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void testFindAllByStatus() {
        final StatusEnum expected = StatusEnum.RETRY;
        final FailureMessage failureMessage = TestData.buildFailureMessage();

        customRepository.insert(failureMessage).block();

        StepVerifier.create(repository.findAllByStatus(expected))
                .expectNextMatches(actual -> actual.getStatus() == expected)
                .verifyComplete();
    }
}
