package com.epam.gym.service;

import com.epam.gym.dto.ChangeStatusRequestDTO;
import com.epam.gym.dto.UserChangePasswordRequestDTO;
import com.epam.gym.entity.User;
import com.epam.gym.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "CHARS", "ABC123");
    }

    @Test
    void generateUsername_shouldReturnSimpleUsername_whenNoDuplicates() {
        when(userRepository.countAllByUsername("john.doe"))
                .thenReturn(0);

        String result = userService.generateUsername("john", "doe");

        assertEquals("john.doe", result);
    }

    @Test
    void generateUsername_shouldAppendNumber_whenDuplicateExists() {
        when(userRepository.countAllByUsername("john.doe"))
                .thenReturn(2);

        String result = userService.generateUsername("john", "doe");

        assertEquals("john.doe2", result);
    }

    @Test
    void generatePassword_shouldReturn10CharacterPassword() {
        String password = userService.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    @Test
    void isUserExists_shouldReturnUser_whenUserActive() {
        User user = new User();
        user.setIsActive(true);

        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "123"))
                .thenReturn(Optional.of(user));

        Optional<User> result = userService.isUserExists("john", "123");

        assertTrue(result.isPresent());
    }

    @Test
    void isUserExists_shouldReturnEmpty_whenUserNotFound() {
        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "123"))
                .thenReturn(Optional.empty());

        Optional<User> result = userService.isUserExists("john", "123");

        assertTrue(result.isEmpty());
    }

    @Test
    void changeStatus_shouldUpdateStatus() {
        User user = new User();
        user.setUsername("john");
        user.setIsActive(true);

        ChangeStatusRequestDTO dto = new ChangeStatusRequestDTO();
        dto.setUsername("john");
        dto.setIsActive(false);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        // act
        assertDoesNotThrow(() -> userService.changeStatus(dto));

        // assert
        assertFalse(user.getIsActive());
        verify(userRepository).save(user);
    }

    @Test
    void changeStatus_shouldThrowException_whenUserNotFound() {
        ChangeStatusRequestDTO dto = new ChangeStatusRequestDTO();
        dto.setUsername("john");
        dto.setIsActive(false);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.changeStatus(dto)
        );
    }

    @Test
    void changePassword_shouldUpdatePassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("old");
        user.setIsActive(true);

        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();
        dto.setUsername("john");
        dto.setOldPassword("old");
        dto.setNewPassword("new123");

        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "old"))
                .thenReturn(Optional.of(user));

        // act
        assertDoesNotThrow(() -> userService.changePassword(dto));

        // assert
        assertEquals("new123", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotFound() {
        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();
        dto.setUsername("john");
        dto.setOldPassword("wrong");
        dto.setNewPassword("new123");

        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "wrong"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.changePassword(dto)
        );
    }
}