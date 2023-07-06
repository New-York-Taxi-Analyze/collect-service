package com.newyorktaxi.repository;

import com.newyorktaxi.entity.FailureMessage;
import reactor.core.publisher.Mono;

public interface CustomFailureMessageRepository {

    Mono<FailureMessage> insert(FailureMessage message);
}
