package com.example.service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.enums.Status;
import com.example.exception.UserAlreadyExistsException;
import com.example.mapper.AuthMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.dto.request.RegistrationRequestDto;
import com.example.dto.response.UserResponseDto;
import com.example.service.user.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentificationServiceImpl implements AuthentificationService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserService userService;


    @Override
    @Transactional
    public UserResponseDto register(RegistrationRequestDto request) {
        String username = request.username();

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists");
        }

        User user = authMapper.toUserForRegister(request);

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new EntityNotFoundException(
                "Role not found"
        ));

        user.setRoles(List.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE  );

        User registered = userRepository.save(user);

        log.info(" IN register - user: {} successfully registered", registered);

        return authMapper.toUserResponse(registered);
    }
    @Transactional
    public UserResponseDto login(String username) {

        User user = userService.findByUsername(username);
        return authMapper.toUserResponse(user);
    }

}
