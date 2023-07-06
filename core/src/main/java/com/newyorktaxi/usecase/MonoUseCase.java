package com.newyorktaxi.usecase;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface MonoUseCase<T, R> {

    Mono<R> execute(@Valid T params);
}
