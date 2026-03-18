package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.mapper.trainee.TraineeMapper;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.repository.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class TraineeService {


    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerService trainerService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserService userService,
                          TrainerService trainerService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
    }

    //2. Create Trainee profile.
    public ApiResponse<AuthDTO> createTrainee(CreateTraineeRequestDTO dto) {
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
        return ApiResponse.ok(new AuthDTO(username, password));
    }

    //6. Select Trainee profile by username.
    // TODO:
    //  [Optional]
    //  You can chain repository result and Optional methods findBy...(...).orElseThrow(...)
    public ApiResponse<TraineeDTO> getTraineeByUsername(String username) {
        Trainee trainee = getTrainee(username);
        return ApiResponse.ok(TraineeMapper.toTraineeDTO(trainee));
    }

    //7. Trainee password change
    public void changePassword(UserChangePasswordRequestDTO dto) {
        userService.changePassword(dto);
    }

    //    10. Update trainee profile.
    public ApiResponse<TraineeDTO> updateTrainee(UpdateTraineeRequestDTO dto) {
        Trainee trainee = getTrainee(dto.getUsername());
        trainee.setAddress(dto.getAddress());
        trainee.setDateOfBirth(dto.getDateOfBirth());
        User user = trainee.getUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setIsActive(dto.getIsActive());
        log.info("Trainee updated{}", trainee.getId());
        trainee = traineeRepository.save(trainee);
        return ApiResponse.ok(TraineeMapper.toTraineeDTO(trainee));
    }

    //    11. Activate/De-activate trainee.
    public ApiResponse<?> changeStatusTrainee(ChangeStatusRequestDTO dto) {
        log.info("Trainee {} activated/deactivated {}", dto.getIsActive(), dto.getUsername());
        return ApiResponse.ok(userService.changeStatus(dto));
    }

    //    13. Delete trainee profile by username.
    // TODO:
    //  What does boolean return type represent in this case?
    @Transactional
    public ApiResponse<?> deleteTrainee(String username) {
        Trainee trainee = getTrainee(username);
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
        }

        trainee.getTrainers().clear();
        log.info("Trainee deleted{}", trainee.getId());
        traineeRepository.delete(trainee);
        return ApiResponse.ok();
    }

    public Trainee getTrainee(String username) {
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found" + username));
    }

    @Transactional
    public @Nullable ApiResponse<List<TrainerDTO>> updateTrainerList(UpdateTrainersRequestDTO dto) {
        Trainee trainee = getTrainee(dto.getTraineeUsername());
        List<Trainer> newTrainers = trainerService.getTrainersByUsernames(dto.getTrainerUsernames());

        new HashSet<>(trainee.getTrainers())
                .forEach(trainee::removeTrainer);

        newTrainers.forEach(trainee::addTrainer);

        traineeRepository.save(trainee);
        return ApiResponse.ok(trainee.getTrainers().stream().map(TrainerMapper::toTrainerDTO).toList());
    }
}

