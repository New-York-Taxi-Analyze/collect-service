package com.newyorktaxi.configuration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final String USERNAME_CLAIM = "preferred_username";

    Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return jwtGrantedAuthoritiesConverter.convert(jwt)
                .collectList()
                .map((authorities) -> new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(USERNAME_CLAIM)));
    }
}

