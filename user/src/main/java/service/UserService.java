package service;

import entity.User;

public interface UserService {
    User findUserById(Long id);
}
