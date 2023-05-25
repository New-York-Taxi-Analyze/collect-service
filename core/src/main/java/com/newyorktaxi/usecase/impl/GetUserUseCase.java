package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.entity.User;
import com.newyorktaxi.repository.UserRepository;
import com.newyorktaxi.usecase.FunctionalUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetUserUseCase implements FunctionalUseCase<String, User> {

    UserRepository userRepository;

    @Override
    public User execute(String params) {
        return userRepository.findByEmail(params)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
