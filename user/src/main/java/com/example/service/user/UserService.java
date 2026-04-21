package com.example.service.user;

import com.example.entity.User;

public interface UserService {

    User findByUsername(String username);
}
