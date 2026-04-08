package com.epam.gym.service;

import com.epam.gym.dto.AuthDTO;
import com.epam.gym.dto.UserChangePasswordRequestDTO;
import com.epam.gym.enums.UserRoleEnum;
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

    @Test
    void login_shouldMapAuthoritiesToUserRoleEnum() {
        AuthDTO dto = new AuthDTO("john", "1234");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        User userDetails = new User(
                "john",
                "1234",
                List.of(new SimpleGrantedAuthority("ROLE_TRAINER"))
        );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtTokenService.encode(eq("john"), any()))
                .thenReturn("token");

        authService.login(dto);

        verify(jwtTokenService).encode(eq("john"),
                argThat(roles -> roles.contains(UserRoleEnum.ROLE_TRAINER)));
    }
    @Test
    void login_shouldHandleMultipleRoles() {
        AuthDTO dto = new AuthDTO("john", "1234");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        User userDetails = new User(
                "john",
                "1234",
                List.of(
                        new SimpleGrantedAuthority("ROLE_TRAINER"),
                        new SimpleGrantedAuthority("ROLE_TRAINEE")
                )
        );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtTokenService.encode(eq("john"), any()))
                .thenReturn("token");

        authService.login(dto);

        verify(jwtTokenService).encode(eq("john"),
                argThat(roles ->
                        roles.size() == 2 &&
                                roles.contains(UserRoleEnum.ROLE_TRAINER) &&
                                roles.contains(UserRoleEnum.ROLE_TRAINEE)
                ));
    }
    @Test
    void login_shouldNotCallLoginFailed_onSuccess() {
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

        when(jwtTokenService.encode(any(), any()))
                .thenReturn("token");

        authService.login(dto);

        verify(loginAttemptService, never()).loginFailed(any());
    }

    @Test
    void login_shouldNotCallLoginSucceeded_onFailure() {
        AuthDTO dto = new AuthDTO("john", "wrong");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> authService.login(dto));

        verify(loginAttemptService, never()).loginSucceeded(any());
    }
    @Test
    void login_shouldNotIncrementCounter_onFailure() {
        AuthDTO dto = new AuthDTO("john", "wrong");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> authService.login(dto));

        verify(counter, never()).increment();
    }
    @Test
    void changePassword_shouldCallUserService() {
        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();

        authService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    @Test
    void login_shouldHandleEmptyAuthorities() {
        AuthDTO dto = new AuthDTO("john", "1234");

        when(loginAttemptService.isBlocked("john")).thenReturn(false);

        User userDetails = new User(
                "john",
                "1234",
                List.of()
        );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(jwtTokenService.encode(any(), any()))
                .thenReturn("token");

        String token = authService.login(dto);

        assertEquals("token", token);
    }
    @Test
    void logout_shouldBlacklistToken() {
        String token = "mocked-jwt-token";
        authService.logout(token);
    }

}