package com.newyorktaxi.controller;

import com.newyorktaxi.mapper.UserParamsMapper;
import com.newyorktaxi.model.UserRequest;
import com.newyorktaxi.usecase.FunctionalUseCase;
import com.newyorktaxi.usecase.params.UserParams;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserController {

    FunctionalUseCase<UserParams, Void> createUserUseCase;
    UserParamsMapper userParamsMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody UserRequest userRequest) {
        final UserParams userParams = userParamsMapper.toUserParams(userRequest);
        createUserUseCase.execute(userParams);
    }
}
