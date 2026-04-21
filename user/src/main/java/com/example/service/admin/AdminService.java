package com.example.service.admin;


import com.example.dto.response.AdminResponseDto;

import java.util.List;

public interface AdminService {

   AdminResponseDto getByUsername(String username);

   List<AdminResponseDto> getAllUsers();

   AdminResponseDto getUserById(Long id);

   void blockUser(Long id);

   void unblockUser(Long id);

   void delete(Long id);

   void deleteAllAdvertisement();

   void addRoleToUser(Long userId, String role);

}
