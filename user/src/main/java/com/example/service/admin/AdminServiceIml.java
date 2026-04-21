package com.example.service.admin;


import com.example.entity.Role;
import com.example.entity.User;
import com.example.enums.Status;
import com.example.repository.AdvertisementRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.mapper.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.dto.response.AdminResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceIml implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AdvertisementRepository advertisementRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        log.info("User found by username: {}", username);

        return userMapper.toAdminResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminResponseDto> getAllUsers() {
        List<User> resultListUser = userRepository.findAll();

        log.info("Found {} users", resultListUser.size());

        return userMapper.toAdminResponseList(resultListUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDto getUserById(Long id) {
        User user = findUserById(id);

        log.info("User found by id: {}", id);

        return userMapper.toAdminResponse(user);
    }

    @Override
    @Transactional
    public void blockUser(Long id) {
        User user = findUserById(id);

        if (user.getStatus() == Status.BLOCKED) {
            throw new IllegalStateException("User is already blocked");
        }

        user.setStatus(Status.BLOCKED);
        userRepository.save(user);

        log.info("User with id {} has been blocked", id);

    }

    @Override
    @Transactional
    public void unblockUser(Long id) {
        User user = findUserById(id);

        if (user.getStatus() != Status.BLOCKED) {
            throw new IllegalStateException("User is already unblocked");
        }

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        log.info("User with id {} has been unblocked", id);

    }


    @Override
    @Transactional
    public void delete(Long id) {
        User user =  findUserById(id);

        user.setStatus(Status.DELETED);
        userRepository.save(user);
        log.info("User with id {} has been deleted", id);
    }

    @Override
    @Transactional
    public void deleteAllAdvertisement() {
        advertisementRepository.deleteAll();
        log.info("All advertisements deleted by admin");
    }

    @Override
    @Transactional
    public void addRoleToUser(Long userId, String roleName) {
        User user = findUserById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role with name" + roleName + "not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);

            log.info("Role {} added to user {}", role, userId);
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }
}







