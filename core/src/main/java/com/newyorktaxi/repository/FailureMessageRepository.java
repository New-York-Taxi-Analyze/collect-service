package com.newyorktaxi.repository;

import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.entity.StatusEnum;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface FailureMessageRepository extends ReactiveCrudRepository<FailureMessage, UUID> {

    Flux<FailureMessage> findAllByStatus(StatusEnum status);
}
