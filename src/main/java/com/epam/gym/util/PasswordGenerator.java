package com.epam.gym.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class PasswordGenerator {
    @Value("${password.characters}")
    private  String CHARS ;

    public String generatePassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return sb.toString();
    }
}
