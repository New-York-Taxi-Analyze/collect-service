package com.newyorktaxi.constants;

import java.util.UUID;

public class SecurityConstants {

    public static final int SECONDS_TO_ADD = 30000000;
    public static final String JWT_KEY = UUID.randomUUID().toString();
    public static final String BEARER = "Bearer ";
}
