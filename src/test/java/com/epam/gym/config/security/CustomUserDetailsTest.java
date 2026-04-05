package com.epam.gym.config.security;

import com.epam.gym.entity.User;
import com.epam.gym.enums.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void constructor_shouldMapRolesToAuthorities() {
        // given
        User user = new User();
        user.setUsername("john");
        user.setPassword("1234");
        user.setIsActive(true);

        List<UserRoleEnum> roles = List.of(
                UserRoleEnum.ROLE_TRAINER,
                UserRoleEnum.ROLE_ADMIN
        );

        // when
        CustomUserDetails details = new CustomUserDetails(user, roles);

        // then
        assertEquals(2, details.getAuthorities().size());

        assertTrue(details.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_TRAINER")
        ));

        assertTrue(details.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        ));
    }

    @Test
    void getUsername_shouldReturnUsernameFromUser() {
        User user = new User();
        user.setUsername("john");

        CustomUserDetails details =
                new CustomUserDetails(user, List.of());

        assertEquals("john", details.getUsername());
    }

    @Test
    void getPassword_shouldReturnPasswordFromUser() {
        User user = new User();
        user.setPassword("secret");

        CustomUserDetails details =
                new CustomUserDetails(user, List.of());

        assertEquals("secret", details.getPassword());
    }

    @Test
    void isAccountNonLocked_shouldReturnUserActiveStatus() {
        User user = new User();
        user.setIsActive(false);

        CustomUserDetails details =
                new CustomUserDetails(user, List.of());

        assertFalse(details.isAccountNonLocked());
    }

    @Test
    void isAccountNonExpired_shouldAlwaysReturnTrue() {
        CustomUserDetails details =
                new CustomUserDetails(new User(), List.of());

        assertTrue(details.isAccountNonExpired());
    }

    @Test
    void isCredentialsNonExpired_shouldAlwaysReturnTrue() {
        CustomUserDetails details =
                new CustomUserDetails(new User(), List.of());

        assertTrue(details.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_shouldAlwaysReturnTrue() {
        CustomUserDetails details =
                new CustomUserDetails(new User(), List.of());

        assertTrue(details.isEnabled());
    }
}