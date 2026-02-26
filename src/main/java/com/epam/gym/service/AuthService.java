package com.epam.gym.service;

import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }
    //3. Trainee username and password matching.
    //4. Trainer username and password matching.
    public User login(String username, String password) {
       Optional<User> user = userService.isUserExists(username, password);
        if (user.isEmpty()) throw new UserNotFoundException("User not found");
        return user.get();
    }
}
