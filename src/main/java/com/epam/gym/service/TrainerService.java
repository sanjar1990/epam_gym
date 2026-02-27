package com.epam.gym.service;

import com.epam.gym.dto.CreateTrainerRequestDTO;
import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.dto.UpdateTrainerRequestDTO;
import com.epam.gym.dto.UserChangePasswordDTO;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TrainerRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Service
@Slf4j
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final AuthService authService;
    private final TrainerAndTraineeService trainerAndTraineeService;


    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserService userService,
                          AuthService authService, TrainerAndTraineeService trainerAndTraineeService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.authService = authService;
        this.trainerAndTraineeService = trainerAndTraineeService;
    }

    //1. Create Trainer profile.
    public void create(CreateTrainerRequestDTO dto) {
        String username = userService.generateUsername(dto.getFirstName(), dto.getLastName());
        String password = userService.generatePassword();

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(true);
        trainer.setUser(user);
        trainer.setTrainingTypeId(dto.getTrainingTypeId());
        trainerRepository.save(trainer);
        log.info("Trainer created: {}", trainer.getId());
    }

    //5. Select Trainer profile by username.
    public Trainer getTrainerByUsername(String username) {
        Optional<Trainer> trainer = trainerRepository.findByUserUsername(username);
        if (trainer.isEmpty()) {
            log.error("User not found with username: {}", username);
            throw new UserNotFoundException("User not found with username: " + username);
        }
        return trainer.get();
    }

    //8. Trainer password change
    public void changePassword(UserChangePasswordDTO dto) {
        log.info("Changing password for user: {}", dto.getUsername());
        userService.changePassword(dto);
    }

    //9. Update trainer profile.
    public boolean updateTrainer(String username, String password, UpdateTrainerRequestDTO dto) {
        //Authentication
        authService.login(username, password);

        Trainer trainer = getTrainerByUsername(username);
        trainer.setTrainingTypeId(dto.getTrainingTypeId());
        User user = trainer.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        trainerRepository.save(trainer);
        log.info("Trainer updated: {}", trainer.getId());
        return true;
    }

    //12. Activate/De-activate trainer.
    public boolean activateDeactivateTrainer(String username, String password) {
        User user = authService.login(username, password);
        log.info("Changing status for user: {}", user.getUsername());
        return userService.changeStatus(user);
    }
//    17. Get trainers list that not assigned on trainee by trainee's username.

    public List<TrainerDTO> getTrainersNotAssignedOnTrainee(String traineeUsername) {
        return trainerAndTraineeService.getTrainersNotAssignedOnTrainee(traineeUsername);
    }

}
