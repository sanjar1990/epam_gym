package com.epam.gym.service;

import com.epam.gym.dto.AuthDTO;
import com.epam.gym.entity.User;
import com.epam.gym.entity.UserRole;
import com.epam.gym.enums.UserRoleEnum;
import com.epam.gym.exceptions.UserNotFoundException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j

public class AuthService {
    private final UserService userService;
    private final Counter userLogedInCounter;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public AuthService(UserService userService, PasswordEncoder passwordEncoder,
                       JwtTokenService jwtTokenService, MeterRegistry registry) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.userLogedInCounter = registry.counter("user.login.count");
    }


    //3. Trainee username and password matching.
    //4. Trainer username and password matching.
    public String login(AuthDTO dto) {
        User user = userService.getUser(dto.getUsername());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.info("User {}  failed to log in", dto.getUsername());
            throw new UserNotFoundException("Invalid username or password");
        }
   List<UserRoleEnum> roleEnumList= user.getRoles().stream().map(UserRole::getRole).toList();
        userLogedInCounter.increment();
        return jwtTokenService.encode(user.getUsername(),roleEnumList);

    }
}
