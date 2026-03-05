package com.epam.gym.service;

import com.epam.gym.dto.UserChangePasswordDTO;
import com.epam.gym.entity.User;
import com.epam.gym.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
@Slf4j
@Service
public class UserService {
    @Value("${password.characters}")
    private String CHARS;
    private final UserRepository userRepository;


    @Autowired()
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateUsername(String firstName, String lastName) {
        String username = firstName + "." + lastName;
        // TODO:
        //  You have defined an unique constraint on username field in User entity which is good.
        //  Now you generate the username filtering only active users and later call repo.save()
        //  What happens if user with the same first and last name already exists but in status active=false?
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
        return userRepository.findByUsernameAndPasswordAndIsActiveTrue(username, password);
    }


    public boolean changeStatus(User user) {
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        return user.getIsActive();
    }

    //7. Trainee password change
    public void changePassword(UserChangePasswordDTO dto) {
        User user = userRepository.findByUsernameAndPasswordAndIsActiveTrue(
                dto.getUsername(), dto.getOldPassword())
                .orElseThrow(() ->{
                    log.error("User not found {}", dto.getUsername());
                    return new RuntimeException("User not found");

                });
        user.setPassword(dto.getNewPassword());
        userRepository.save(user);
    }

}

