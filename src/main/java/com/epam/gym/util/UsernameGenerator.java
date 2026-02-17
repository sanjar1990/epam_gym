package com.epam.gym.util;

import com.epam.gym.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
public class UsernameGenerator {
    public String generateUsername(String firstName,
                                   String lastName,
                                   Collection<? extends User> existingUsers) {

        String base = firstName + "." + lastName;

        long count = existingUsers.stream()
                .filter(u -> u.getUsername().startsWith(base))
                .count();

        return count == 0 ? base : base + count;
    }
}
