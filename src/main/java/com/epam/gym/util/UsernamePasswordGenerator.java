package com.epam.gym.util;

import com.epam.gym.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Random;

@Component
// TODO:
//  Single Responsibility Principle violation.
//  A canonical definition of SRP - class should have only one reason to change.
//  In addition, username & password generation methods do not share common logic.
//  So it is better to have a separate class for each of them. Example: UsernameGenerator and PasswordGenerator
//  In general, do not hesitate to use many small focused classes - will be much easier to maintain when project grows.
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
