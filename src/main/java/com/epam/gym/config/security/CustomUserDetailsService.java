package com.epam.gym.config.security;

import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.UserRepository;
import com.epam.gym.service.UserRoleService;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;


    @Override

    public UserDetails loadUserByUsername(String  username) throws UsernameNotFoundException {
        Optional<User> optionalOfUser = userRepository.findByUsername(username);
        if (optionalOfUser.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }
        return new CustomUserDetails(optionalOfUser.get(),
                userRoleService.getByProfileId(optionalOfUser.get().getId()));
    }
}