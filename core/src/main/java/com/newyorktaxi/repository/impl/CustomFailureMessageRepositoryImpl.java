package com.newyorktaxi.repository.impl;

import com.newyorktaxi.entity.FailureMessage;
import com.newyorktaxi.repository.CustomFailureMessageRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomFailureMessageRepositoryImpl implements CustomFailureMessageRepository {

    R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<FailureMessage> insert(FailureMessage message) {
        return r2dbcEntityTemplate.insert(FailureMessage.class)
                .using(message)
                .thenReturn(message);
    }
}

