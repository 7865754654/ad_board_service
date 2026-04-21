package com.example.jwt.converter;

import com.example.exception.JwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Base64;

public class BasicAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Basic ")) {
            throw new JwtAuthenticationException("Заголовок Basic отсутсвует");

        }

            String headerBasic = header.substring(6);
            String credentials = new String(Base64.getDecoder().decode(headerBasic));

            String[] partsCredentials = credentials.split(":", 2);

            return new UsernamePasswordAuthenticationToken(partsCredentials[0], partsCredentials[1]);
    }
}
