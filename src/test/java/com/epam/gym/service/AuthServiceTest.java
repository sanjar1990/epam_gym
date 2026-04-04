package com.epam.gym.service;

import com.epam.gym.dto.AuthDTO;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private MeterRegistry registry;

    private AuthService authService;
    @Mock
    private Counter trainingCreatedCounter;

    @BeforeEach
    void setup() {
        when(registry.counter(anyString()))
                .thenReturn(trainingCreatedCounter);

        authService = new AuthService(userService, registry);
    }


    @Test
    void login_shouldPass_whenUserExists() {
        // given
        String username = "john";
        String password = "1234";

        AuthDTO dto = new AuthDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        User mockUser = new User();
        mockUser.setUsername(username);

        when(userService.getUser(username))
                .thenReturn(Optional.of(mockUser));

        // when + then (no exception expected)
        assertDoesNotThrow(() -> authService.login(dto));

        verify(userService, times(1))
                .getUser(username);
    }

    @Test
    void login_shouldThrowException_whenUserDoesNotExist() {
        // given
        String username = "wrong";
        String password = "wrong";

        AuthDTO dto = new AuthDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        when(userService.isUserExists(username, password))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class, () ->
                authService.login(dto)
        );

        verify(userService, times(1))
                .isUserExists(username, password);
    }
}