package com.epam.gym.service;

import com.epam.gym.dto.ApiResponse;
import com.epam.gym.dto.AuthDTO;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }


    //3. Trainee username and password matching.
    //4. Trainer username and password matching.
    public ApiResponse<?> login(AuthDTO dto) {
        Optional<User> user = userService.isUserExists(dto.getUsername(), dto.getPassword());
        if (user.isEmpty()) {
            log.info("User {}  failed to log in", dto.getUsername());
            throw new UserNotFoundException("User not found");
        }
        return ApiResponse.ok();
    }
}
