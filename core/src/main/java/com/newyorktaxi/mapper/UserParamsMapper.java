package com.newyorktaxi.mapper;

import com.newyorktaxi.model.AuthResponse;
import com.newyorktaxi.model.UserRequest;
import com.newyorktaxi.usecase.params.UserParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserParamsMapper {

    @Mapping(target = "role", constant = "USER")
    UserParams toUserParams(UserRequest userRequest);

    AuthResponse toAuthResponse(String token);
}
