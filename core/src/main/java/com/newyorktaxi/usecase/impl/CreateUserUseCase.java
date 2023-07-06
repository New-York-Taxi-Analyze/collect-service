package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.entity.User;
import com.newyorktaxi.mapper.UserMapper;
import com.newyorktaxi.repository.UserRepository;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.UserParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserUseCase implements MonoUseCase<UserParams, Void> {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public Mono<Void> execute(UserParams params) {
        final String encode = passwordEncoder.encode(params.getPassword());
        final User user = userMapper.toUser(params, encode);

        return userRepository.save(user).then();
    }
}
