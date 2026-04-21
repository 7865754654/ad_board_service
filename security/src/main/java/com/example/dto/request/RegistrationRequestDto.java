package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @NotBlank(message = "First name is required")
        @Size(min = 4, max = 50)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 4, max = 50)
        String lastName,

        @NotBlank(message = "Email is required")
        @Size(min = 5, max = 100)
        String email,

        @NotBlank(message = "Username is required")
        @Size(min = 10, max = 50)
        String username,

        @NotBlank(message = "Password  is required")
        @Size(min = 5, max = 20)
        String password,

        @NotBlank(message = "Phone is required")
        String phone
) {
}
