package com.newyorktaxi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ROLES = "roles";

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS);

        if (realmAccess == null || realmAccess.isEmpty()) {
            return null;
        }

        return ((Collection<String>) realmAccess.get(ROLES)).stream()
                .map(roleName -> ROLE_PREFIX + roleName)
                .map(roleName -> (GrantedAuthority) () -> roleName)
                .collect(Collectors.toList());
    }
}
