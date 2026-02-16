package com.epam.gym.util;

import com.epam.gym.entity.User;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Random;

@Component
public class UsernamePasswordGenerator {
    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public String generatePassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return sb.toString();
    }
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
