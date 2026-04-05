package com.epam.gym.service;

import com.epam.gym.dto.AuthDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthService authService;


    @BeforeEach
    void setup() {
        when(meterRegistry.counter("user.login.count"))
                .thenReturn(counter);

        authService = new AuthService(
                userService,
                jwtTokenService,
                meterRegistry,
                authenticationManager,
                loginAttemptService
        );
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        // given
        AuthDTO dto = new AuthDTO("john", "1234");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        User userDetails = new User(
                "john",
                "1234",
                List.of(new SimpleGrantedAuthority("ROLE_TRAINEE"))
        );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtTokenService.encode(eq("john"), any()))
                .thenReturn("mocked-jwt-token");

        // when
        String token = authService.login(dto);

        // then
        assertEquals("mocked-jwt-token", token);

        verify(loginAttemptService).loginSucceeded("john");
        verify(counter).increment();
    }

    @Test
    void login_shouldThrowException_whenUserIsBlocked() {
        // given
        AuthDTO dto = new AuthDTO("john", "1234");

        when(loginAttemptService.isBlocked("john")).thenReturn(true);

        // when + then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(dto));

        assertEquals("User is blocked. Try again later.", ex.getMessage());

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {
        // given
        AuthDTO dto = new AuthDTO("john", "wrong");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        // when + then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(dto));

        assertEquals("Invalid username or password", ex.getMessage());

        verify(loginAttemptService).loginFailed("john");
    }
}