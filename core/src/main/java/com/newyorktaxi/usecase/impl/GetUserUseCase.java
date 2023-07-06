package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.entity.User;
import com.newyorktaxi.repository.UserRepository;
import com.newyorktaxi.usecase.MonoUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetUserUseCase implements MonoUseCase<String, User> {

    UserRepository userRepository;

    @Override
    public Mono<User> execute(String params) {
        return userRepository.findByEmail(params);
    }
}
