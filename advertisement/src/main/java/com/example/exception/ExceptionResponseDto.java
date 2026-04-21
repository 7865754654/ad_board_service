package com.example.exception;

import java.time.LocalDateTime;

public record ExceptionResponseDto (
        String message,
        String detailsMessage,
        LocalDateTime errorTime
) {}
