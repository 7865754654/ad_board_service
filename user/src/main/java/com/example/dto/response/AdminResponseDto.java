package com.example.dto.response;

import com.example.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public record AdminResponseDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        List<String> roles,
        LocalDateTime created,
        LocalDateTime updated,
        Status status
) {
}
