package com.epam.gym.service;

import com.epam.gym.dto.CreateTraineeCreateRequestDTO;
import com.epam.gym.dto.UpdateTraineeRequestDTO;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TraineeService {


    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserService userService,
                          AuthService authService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.authService = authService;
    }

//2. Create Trainee profile.

    public void create(CreateTraineeCreateRequestDTO dto) {
        String username = userService.generateUsername(dto.getFirstName(), dto.getLastName());
        String password = userService.generatePassword();

        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);

        trainee.setAddress(dto.getAddress());
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setUser(user);

        traineeRepository.save(trainee);
    }

    //6. Select Trainee profile by username.
    public Trainee getTraineeByUsername(String username) {
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
        if (optionalTrainee.isEmpty()) throw new UserNotFoundException("Trainee not found" + username);
        return optionalTrainee.get();
    }

    //7. Trainee password change
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = authService.login(username, oldPassword);
        user.setPassword(newPassword);
        return userService.changePassword(user);
    }

    //    10. Update trainee profile.
    public boolean updateTrainee(String username, String password, UpdateTraineeRequestDTO dto) {
//Authentication
        authService.login(username, password);

        Trainee trainee = getTraineeByUsername(username);
        trainee.setAddress(dto.getAddress());
        trainee.setDateOfBirth(dto.getDateOfBirth());
        User user = trainee.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        traineeRepository.save(trainee);
        return true;
    }

    //    11. Activate/De-activate trainee.
    public boolean activateDeactivateTrainer(String username, String password) {
        User user = authService.login(username, password);
        return userService.changeStatus(user);
    }

//    13. Delete trainee profile by username.
    public boolean deleteTrainee(String username, String password) {
        User user = authService.login(username, password);
        Trainee trainee = getTraineeByUsername(username);
        traineeRepository.delete(trainee);
        return true;
    }

}
