package com.example.mapper;

import com.example.dto.request.RegistrationRequestDto;
import com.example.dto.response.UserResponseDto;
import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "advertisements", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User toUserForRegister(RegistrationRequestDto registrationRequestDto);


    @Mapping(target = "authorities", ignore = true)
    UserResponseDto toUserResponse(User user);
}
