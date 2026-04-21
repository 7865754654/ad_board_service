package com.example.config;

import com.example.exception.handler.CustomAccessDeniedHandler;
import com.example.exception.handler.CustomAuthenticationEntryPoint;
import com.example.filter.BasicAuthenticationFilter;
import com.example.filter.JwtAuthenticationFilter;
import com.example.filter.JwtTokenRefreshFilter;
import com.example.jwt.converter.JwtConverter;

import com.example.jwt.converter.deserializer.TokenDeserializer;
import com.example.jwt.converter.serializer.TokenSerializer;
import com.example.jwt.factory.TokenFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;


import java.util.ArrayList;
import java.util.List;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthService;

    private  final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private  final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final TokenFactory accessTokenFactory;
    private final TokenSerializer accessTokenSerializer;
    private final TokenDeserializer refreshTokenDeserializer;
    private final ObjectMapper objectMapper;

    public SecurityConfig(UserDetailsService userDetailsService,
                          AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthService,
                          CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler,
                          @Qualifier("accessTokenFactory") TokenFactory accessTokenFactory,
                          @Qualifier("accessTokenSerializer") TokenSerializer accessTokenSerializer,
                          @Qualifier("refreshTokenDeserializer") TokenDeserializer refreshTokenDeserializer,
                          ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.preAuthService = preAuthService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenSerializer = accessTokenSerializer;
        this.refreshTokenDeserializer = refreshTokenDeserializer;
        this.objectMapper = objectMapper;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtConverter jwtConverter,
                                                           CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                           AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(authenticationManager, jwtConverter, authenticationEntryPoint);
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(AuthenticationManager authenticationManager,
                                                               CustomAuthenticationEntryPoint authenticationEntryPoint) {
        return new BasicAuthenticationFilter(authenticationManager, authenticationEntryPoint);
    }

    @Bean
    public JwtTokenRefreshFilter jwtTokenRefreshFilter() {
        return new JwtTokenRefreshFilter(
                accessTokenFactory,
                accessTokenSerializer,
                refreshTokenDeserializer,
                objectMapper
        );
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<>();

        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(preAuthService);
        providers.add(preAuthProvider);

        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        providers.add(daoProvider);


        return new ProviderManager(providers);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           BasicAuthenticationFilter basicAuthenticationFilter,
                                           JwtTokenRefreshFilter jwtTokenRefreshFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/advertisement/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/advertisement/filter").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/advertisement/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/advertisement/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/advertisement/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenRefreshFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(basicAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler);
                })
                .build();
    }
}
