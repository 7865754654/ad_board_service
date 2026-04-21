package com.example.jwt.converter;

import com.example.exception.JwtAuthenticationException;
import com.example.jwt.converter.deserializer.TokenDeserializer;
import com.example.jwt.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtConverter implements AuthenticationConverter {

    private final TokenDeserializer accessTokenJwsDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

       String token = header.substring(7);
       Token accessToken = accessTokenJwsDeserializer.deserialize(token);

       if (accessToken == null) {
            throw new JwtAuthenticationException("AccessToken is null");
       }

       if (accessToken.expiresAt().isBefore(Instant.now())) {
           throw new JwtAuthenticationException("Token expired");
       }

       List<SimpleGrantedAuthority> authorities = accessToken.authorities().stream()
              .map(role -> new SimpleGrantedAuthority(role))
              .collect(Collectors.toList());

       PreAuthenticatedAuthenticationToken authenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(
              accessToken,
              token
       );

       return authenticatedAuthenticationToken;
    }
}

