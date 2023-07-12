package com.newyorktaxi.repository;

import com.newyorktaxi.TestData;
import com.newyorktaxi.entity.FailureMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ComponentScan(basePackages = {"com.newyorktaxi.repository"})
class CustomFailureMessageRepositoryTest {

    @Autowired
    FailureMessageRepository repository;

    @Autowired
    CustomFailureMessageRepository customRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("Should successfully insert a FailureMessage")
    void testInsert() {
        final FailureMessage failureMessage = TestData.buildFailureMessage();

        StepVerifier.create(customRepository.insert(failureMessage))
                .expectNextMatches(actual -> actual.equals(failureMessage))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw DuplicateKeyException when insert twice the same value")
    void testInsertTwiceTheSameValue() {
        final FailureMessage failureMessage = TestData.buildFailureMessage();

        StepVerifier.create(customRepository.insert(failureMessage))
                .expectNextMatches(actual -> actual.equals(failureMessage))
                .verifyComplete();

        StepVerifier.create(customRepository.insert(failureMessage))
                .expectError(DuplicateKeyException.class)
                .verify();
    }
}
