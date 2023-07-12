package com.newyorktaxi.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.newyorktaxi.constants.SecurityConstants;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String jwt = authentication.getCredentials().toString();
        final Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.JWT_KEY.getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();

        return Mono.fromCallable(() -> verifier.verify(jwt))
                .map(decodedJWT -> {
                    final String username = decodedJWT.getClaim("username").asString();
                    final List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                });
    }
}
