package com.example.filter;

import com.example.jwt.converter.deserializer.RefreshTokenJweDeserializer;
import com.example.jwt.converter.deserializer.TokenDeserializer;
import com.example.jwt.converter.serializer.TokenSerializer;
import com.example.jwt.factory.AccessTokenFactory;
import com.example.jwt.factory.TokenFactory;
import com.example.jwt.models.Token;
import com.example.jwt.models.Tokens;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;

public class JwtTokenRefreshFilter extends OncePerRequestFilter {

    @Qualifier("accessTokenFactory")
    private final TokenFactory accessTokenFactory;

    @Qualifier("accessTokenSerializer")
    private final TokenSerializer accessTokenSerializer;

    @Qualifier("refreshTokenDeserializer")
    private final TokenDeserializer refreshTokenDeserializer;

    private final ObjectMapper objectMapper;

    private final RequestMatcher requestMatcherRefresh = new AntPathRequestMatcher("/api/jwt/refresh", HttpMethod.POST.name());


    public JwtTokenRefreshFilter(
            TokenFactory accessTokenFactory,
            TokenSerializer accessTokenSerializer,
            TokenDeserializer refreshTokenDeserializer,
            ObjectMapper objectMapper
    ) {
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenSerializer = accessTokenSerializer;
        this.refreshTokenDeserializer = refreshTokenDeserializer;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (requestMatcherRefresh.matches(request)) {

                String refreshTokenString = request.getHeader("Refresh-Token");

                Token refreshToken = refreshTokenDeserializer.deserialize(refreshTokenString);

                if (refreshToken != null && refreshToken.authorities().contains("JWT_REFRESH")) {
                    if (refreshToken.expiresAt().isBefore(Instant.now())) {
                        response.sendError(401, "Token expired");
                        return;
                    }
                    Token accessToken = accessTokenFactory.create(refreshToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(),
                            new Tokens(
                                    accessTokenSerializer.serializer(accessToken),
                                    accessToken.expiresAt().toString(),
                                    null,
                                    null
                            ));
                    return;
                }
                response.sendError(401, "Invalid refresh token");
                return;
        }

        filterChain.doFilter(request, response);
    }
}
