package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

public record UserResponseDto(
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime created,
        Collection<? extends GrantedAuthority> authorities
) { }
