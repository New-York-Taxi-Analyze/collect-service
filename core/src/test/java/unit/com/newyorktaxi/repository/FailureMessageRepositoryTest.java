package com.newyorktaxi.repository;

import com.newyorktaxi.TestData;
import com.newyorktaxi.entity.FailureMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FailureMessageRepositoryTest {

    @Autowired
    FailureMessageRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully save failure message")
    void testSave() {
        final FailureMessage expected = TestData.buildFailureMessage();

        final FailureMessage actual = repository.save(expected);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("key")
                .as("actual does not match expected")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Should successfully find all failure messages by status")
    void testFindAllByStatus() {
        final FailureMessage expected = TestData.buildFailureMessage();
        repository.save(expected);

        final FailureMessage actual = repository.findAllByStatus(expected.getStatus()).get(0);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("key")
                .as("actual does not match expected")
                .isEqualTo(expected);
    }
}
