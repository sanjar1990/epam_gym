package com.epam.gym.service;

import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
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

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnUser_whenUserExists() {
        // given
        String username = "john";
        String password = "1234";

        User mockUser = new User();
        mockUser.setUsername(username);

        when(userService.isUserExists(username, password))
                .thenReturn(Optional.of(mockUser));

        // when
        User result = authService.login(username, password);

        // then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userService, times(1))
                .isUserExists(username, password);
    }

    @Test
    void login_shouldThrowException_whenUserDoesNotExist() {
        // given
        String username = "wrong";
        String password = "wrong";

        when(userService.isUserExists(username, password))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () ->
                authService.login(username, password)
        );

        verify(userService, times(1))
                .isUserExists(username, password);
    }
}