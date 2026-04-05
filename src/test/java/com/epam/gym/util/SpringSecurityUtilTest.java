package com.epam.gym.util;

import com.epam.gym.config.security.CustomUserDetails;
import com.epam.gym.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpringSecurityUtilTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_shouldReturnUser_whenAuthenticationExists() {
        // Arrange
        User user = new User();
        user.setId(1L);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUser()).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        User result = SpringSecurityUtil.getCurrentUser();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCurrentUser_shouldThrowException_whenAuthenticationIsNull() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(SecurityException.class, SpringSecurityUtil::getCurrentUser);
    }

    @Test
    void getCurrentUser_shouldThrowException_whenPrincipalIsNull() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act & Assert
        assertThrows(SecurityException.class, SpringSecurityUtil::getCurrentUser);
    }

    @Test
    void getCurrentUserId_shouldReturnId_whenPrincipalIsCustomUserDetails() {
        // Arrange
        User user = new User();
        user.setId(99L);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUser()).thenReturn(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        Long result = SpringSecurityUtil.getCurrentUserId();

        // Assert
        assertEquals(99L, result);
    }

    @Test
    void getCurrentUserId_shouldReturnNull_whenPrincipalIsNotCustomUserDetails() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        Long result = SpringSecurityUtil.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_shouldThrowException_whenAuthenticationIsNull() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(NullPointerException.class, SpringSecurityUtil::getCurrentUserId);
    }
}