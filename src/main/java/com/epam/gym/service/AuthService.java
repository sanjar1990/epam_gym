package com.epam.gym.service;

import com.epam.gym.dto.AuthDTO;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j

public class AuthService {
    private final UserService userService;
    private final Counter trainingCreatedCounter;

    @Autowired
    public AuthService(UserService userService, MeterRegistry registry) {
        this.userService = userService;
        this.trainingCreatedCounter = Counter.builder("user.login.count")
                .description("Number of users logged in")
                .register(registry);
    }


    //3. Trainee username and password matching.
    //4. Trainer username and password matching.
    public void login(AuthDTO dto) {
        Optional<User> user = userService.isUserExists(dto.getUsername(), dto.getPassword());
        if (user.isEmpty()) {
            log.info("User {}  failed to log in", dto.getUsername());
            throw new UserNotFoundException("User not found");
        }
        trainingCreatedCounter.increment();
    }
}
