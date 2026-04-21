package com.example.filter;

import com.example.exception.JwtAuthenticationException;
import com.example.jwt.converter.BasicAuthenticationConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//нужно
public class BasicAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final BasicAuthenticationConverter basicConverter = new BasicAuthenticationConverter();
    private final RequestMatcher requestMatcherRefresh = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());


    public BasicAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcherRefresh.matches(request)) {
            Authentication authenticationBefore = basicConverter.convert(request);

            if (authenticationBefore != null) {
                try {
                    Authentication authenticationAfter = authenticationManager.authenticate(authenticationBefore);
                    SecurityContextHolder.getContext().setAuthentication(authenticationAfter);
                } catch (JwtAuthenticationException e) {
                    SecurityContextHolder.clearContext();
                    authenticationEntryPoint.commence(request,response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

