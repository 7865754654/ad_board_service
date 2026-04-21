package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = 10, max = 50, message = "Username must be between 10 and 50")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 5, max = 20, message = "Password must be between 5 and 20")
        String password
) {
}
