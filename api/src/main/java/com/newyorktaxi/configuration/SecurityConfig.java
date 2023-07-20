package com.newyorktaxi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
        http.authorizeExchange(authorize -> authorize
                        .pathMatchers("/api/v1/total", "/api/v1/message").authenticated()
                        .pathMatchers("/api/v1/hello").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer.jwt((jwt) -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );

        return http.build();
    }

    @Bean
    Converter<Jwt, Flux<GrantedAuthority>> jwtAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter) {
        return new ReactiveJwtGrantedAuthoritiesConverterAdapter(keycloakGrantedAuthoritiesConverter);
    }
}
