package com.epam.gym.config.security;

import com.epam.gym.entity.User;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// TODO:
//  1. File formatting again  DONE
//  2. Why not constructor injection? DONE
//  3. [Optional] 'loadUserByUsername' method can be simplified by using '.map' + '.orElseThrow' DONE
@Service
@NullMarked
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;


    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(user,
                userRoleService.getByProfileId(user.getId()));
    }
}