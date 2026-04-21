package com.example.dto.response;


import com.example.jwt.models.Tokens;

public record AuthenticationResponse(
        String message,
        Tokens token,
        UserResponseDto user
) {
}
