package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.repository.TrainerRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service
@Slf4j
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;


    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserService userService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
    }

    //1. Create Trainer profile.
    public AuthDTO createTrainer(CreateTrainerRequestDTO dto) {
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
        return new AuthDTO(username, password);
    }

    //5. Select Trainer profile by username.

    //  [Optional]
    //  You can chain repository result and Optional methods findBy...(...).orElseThrow(...)
//    DONE
    public TrainerDTO getTrainerByUsername(String username) {
        Trainer trainer = getTrainerEntityByUsername(username);
        return TrainerMapper.toTrainerDTO(trainer);
    }

    //8. Trainer password change
    public void changePassword(UserChangePasswordRequestDTO dto) {
        log.info("Changing password for user: {}", dto.getUsername());
        userService.changePassword(dto);
    }

    //9. Update trainer profile.
    public TrainerDTO updateTrainer(UpdateTrainerRequestDTO dto) {
        Trainer trainer = getTrainerEntityByUsername(dto.getUsername());
        trainer.setTrainingTypeId(dto.getTrainingTypeId());
        User user = trainer.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setIsActive(dto.getIsActive());
        trainerRepository.save(trainer);
        log.info("Trainer updated: {}", trainer.getId());
        return TrainerMapper.toTrainerDTO(trainer);
    }

    //12. Activate/De-activate trainer.
    public void changeStatusTrainee(ChangeStatusRequestDTO dto) {
        log.info("Changing status for user: {}", dto.getUsername());
         userService.changeStatus(dto);
    }

    //    17. Get trainers list that not assigned on trainee by trainee's username.
    public List<TrainerDTO> getTrainersNotAssignedOnTrainee(String traineeUsername) {
        return trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername)
                .stream().map(TrainerMapper::toTrainerDTO).toList();
    }

    public Trainer getTrainerEntityByUsername(String username) {
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public List<Trainer> getTrainersByUsernames(List<String> trainerUsernames) {
        return trainerRepository.findByUserUsernameIn(trainerUsernames);
    }
}
