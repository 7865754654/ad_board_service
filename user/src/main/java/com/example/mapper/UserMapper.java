package com.example.mapper;

import com.example.entity.Role;
import com.example.entity.User;
import org.mapstruct.Mapper;

import com.example.dto.response.AdminResponseDto;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AdminResponseDto toAdminResponse(User user);

    List<AdminResponseDto> toAdminResponseList(List<User> user);

    default String map(Role role) {
        return role.getName();
    }

}
