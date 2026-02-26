package com.epam.gym.service;

import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserBlockedException;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Value("${password.characters}")
    private String CHARS;
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        int count = userRepository.countAllByIsActiveTrueAndUsername(username);
        return count == 0 ? username : username + count;
    }


    public String generatePassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }

        return sb.toString();
    }
//3. Trainee username and password matching.
//4. Trainer username and password matching.
    public Optional<User> isUserExists(String username, String password) {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent() && user.get().getIsActive()) {
            return user;
        } else {
            return Optional.empty();
        }
    }

    public boolean changePassword(User user) {
        userRepository.save(user);
        return true;
    }

    public boolean changeStatus(User user) {
        user.setIsActive(!user.getIsActive());
       userRepository.save(user);
        return user.getIsActive();
    }

}

