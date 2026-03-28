package service;

import entity.User;
import repository.UserRepository;

import java.util.Optional;

public class UserServiceIml implements UserService{
    public UserRepository userRepository;

    @Override
    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        return user;
    }
}
