package com.example.filter;

import com.example.exception.JwtAuthenticationException;
import com.example.jwt.converter.JwtConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConverter jwtConverter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtConverter jwtConverter, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.jwtConverter = jwtConverter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/api/jwt/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication preAuthenticationToken = jwtConverter.convert(request);

            if (preAuthenticationToken != null) {
                Authentication authenticatedToken = authenticationManager.authenticate(preAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            }
        }
    catch (JwtAuthenticationException e) {
        SecurityContextHolder.clearContext();
        authenticationEntryPoint.commence(request,response, e);
        return;
    }
        filterChain.doFilter(request, response);
    }
}

