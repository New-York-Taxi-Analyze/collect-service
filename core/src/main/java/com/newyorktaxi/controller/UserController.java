package com.newyorktaxi.controller;

import com.newyorktaxi.mapper.UserParamsMapper;
import com.newyorktaxi.model.UserRequest;
import com.newyorktaxi.usecase.MonoUseCase;
import com.newyorktaxi.usecase.params.UserParams;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserController {

    MonoUseCase<UserParams, Void> createUserUseCase;
    UserParamsMapper userParamsMapper;
    MonoUseCase<UserParams, String> getJwtUseCase;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .map(userParamsMapper::toUserParams)
                .flatMap(createUserUseCase::execute)
                .then(Mono.defer(() -> ServerResponse
                        .ok()
                        .build()));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .map(userParamsMapper::toUserParams)
                .flatMap(getJwtUseCase::execute)
                .flatMap(jwt -> ServerResponse.ok()
                        .bodyValue(userParamsMapper.toAuthResponse(jwt)));
    }
}
