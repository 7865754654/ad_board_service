package com.example.jwt.factory;

import com.example.jwt.models.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

@Component("refreshTokenFactory")
public class RefreshTokenFactory implements TokenFactory<Authentication>{

    @Override
    public Duration LifeTime() {
        return Duration.ofDays(1);
    }

    @Override
    public Token create(Authentication authentication) {


        Instant now = Instant.now();
        LinkedList<String> authorities = new LinkedList<>();

        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .forEach(authorities::add);

        return new Token(
                UUID.randomUUID(),
                authentication.getName(),
                authorities,
                now,
                now.plus(LifeTime()));
    }
}
