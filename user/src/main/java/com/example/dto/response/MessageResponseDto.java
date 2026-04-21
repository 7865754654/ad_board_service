package com.example.dto.response;

import java.time.LocalDateTime;

public record MessageResponseDto(
        boolean success,
        String message,
        Long userId,
        LocalDateTime timestamp
) {
}
