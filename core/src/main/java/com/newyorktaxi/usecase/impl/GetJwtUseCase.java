package com.newyorktaxi.usecase.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.newyorktaxi.constants.SecurityConstants;
import com.newyorktaxi.entity.User;
import com.newyorktaxi.repository.UserRepository;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.UserParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetJwtUseCase implements MonoUseCase<UserParams, String> {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> execute(UserParams params) {
        return userRepository.findByEmail(params.getEmail())
                .filter(user -> passwordEncoder.matches(params.getPassword(), user.getPassword()))
                .flatMap(this::encodePassword);
    }

    private Mono<String> encodePassword(User user) {
        final Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.JWT_KEY.getBytes());
        final String jwt = JWT.create()
                .withClaim("username", user.getEmail())
                .withClaim("authorities", List.of(user.getRole())) // FIXME: only one role
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(SecurityConstants.SECONDS_TO_ADD))
                .sign(algorithm);

        return Mono.just(jwt);
    }
}
