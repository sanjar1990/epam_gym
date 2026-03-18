package com.epam.gym.service;

import com.epam.gym.dto.ApiResponse;
import com.epam.gym.dto.AuthDTO;
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
    void login_shouldReturnOk_whenUserExists() {
        // given
        String username = "john";
        String password = "1234";

        AuthDTO dto = new AuthDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        User mockUser = new User();
        mockUser.setUsername(username);

        when(userService.isUserExists(username, password))
                .thenReturn(Optional.of(mockUser));

        // when
        ApiResponse<?> response = authService.login(dto);

        // then
        assertNotNull(response);
        assertFalse(response.getIsError());

        verify(userService, times(1))
                .isUserExists(username, password);
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


        assertThrows(UserNotFoundException.class, () ->
                authService.login(dto)
        );

        verify(userService, times(1))
                .isUserExists(username, password);
    }
}