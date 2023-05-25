package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.entity.User;
import com.newyorktaxi.mapper.UserMapper;
import com.newyorktaxi.repository.UserRepository;
import com.newyorktaxi.usecase.FunctionalUseCase;
import com.newyorktaxi.usecase.params.UserParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserUseCase implements FunctionalUseCase<UserParams, Void> {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public Void execute(UserParams params) {
        final User user = userMapper.toUser(params, passwordEncoder.encode(params.getPassword()));
        userRepository.save(user);
        return null;
    }
}
