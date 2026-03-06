package com.epam.gym.service;


import com.epam.gym.dto.UserChangePasswordDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // manually inject @Value field
        ReflectionTestUtils.setField(userService, "CHARS", "ABC123");
    }

    // -------------------------
    // generateUsername()
    // -------------------------

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

    // -------------------------
    // generatePassword()
    // -------------------------

    @Test
    void generatePassword_shouldReturn10CharacterPassword() {
        String password = userService.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());
    }

    // -------------------------
    // isUserExists()
    // -------------------------

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
    void isUserExists_shouldReturnEmpty_whenUserInactive() {

        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "123"))
                .thenReturn(Optional.empty());

        Optional<User> result = userService.isUserExists("john", "123");

        assertTrue(result.isEmpty());
    }

    @Test
    void isUserExists_shouldReturnEmpty_whenUserNotFound() {
        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue("john", "123"))
                .thenReturn(Optional.empty());

        Optional<User> result = userService.isUserExists("john", "123");

        assertTrue(result.isEmpty());
    }

    // -------------------------
    // changeStatus()
    // -------------------------

    @Test
    void changeStatus_shouldToggleStatus() {
        User user = new User();
        user.setIsActive(true);

        boolean result = userService.changeStatus(user);

        assertFalse(result);
        verify(userRepository).save(user);
    }

    // -------------------------
    // changePassword()
    // -------------------------

    @Test
    void changePassword_shouldUpdatePassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("old");
        user.setIsActive(true);

        UserChangePasswordDTO dto = new UserChangePasswordDTO();
        dto.setUsername("john");
        dto.setOldPassword("old");
        dto.setNewPassword("new123");

        when(userRepository.findByUsernameAndPasswordAndIsActiveTrue(
                "john", "old"))
                .thenReturn(Optional.of(user));

        userService.changePassword(dto);

        assertEquals("new123", user.getPassword());
        verify(userRepository).save(user);
    }
}