package com.example.service;

import com.example.dto.request.RegistrationRequestDto;
import com.example.dto.response.UserResponseDto;

public interface AuthentificationService {
    UserResponseDto register(RegistrationRequestDto registrationRequestDto);

    UserResponseDto login(String username);

}
