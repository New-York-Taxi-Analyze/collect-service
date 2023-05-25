package com.newyorktaxi.usecase.params;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParams {

    String email;
    String password;
    String role;
}
