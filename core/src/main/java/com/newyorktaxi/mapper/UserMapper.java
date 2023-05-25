package com.newyorktaxi.mapper;

import com.newyorktaxi.entity.User;
import com.newyorktaxi.usecase.params.UserParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    User toUser(UserParams userParam, String password);
}
