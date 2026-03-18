package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.TrainingType;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void createTrainer_shouldCreateTrainer_andReturnAuthDTO() {
        CreateTrainerRequestDTO dto = new CreateTrainerRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Smith");
        dto.setTrainingTypeId(1L);

        when(userService.generateUsername("John", "Smith"))
                .thenReturn("john.smith");

        when(userService.generatePassword())
                .thenReturn("pass123");

        ApiResponse<AuthDTO> response = trainerService.createTrainer(dto);

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertEquals("john.smith", response.getData().getUsername());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void getTrainerByUsername_shouldReturnDTO_whenExists() {
        User user = new User();
        user.setUsername("john");

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        ApiResponse<TrainerDTO> response =
                trainerService.getTrainerByUsername("john");

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertNotNull(response.getData());

        verify(trainerRepository).findByUserUsername("john");
    }

    @Test
    void getTrainerByUsername_shouldThrowException_whenNotFound() {
        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> trainerService.getTrainerByUsername("john"));
    }

    @Test
    void changePassword_shouldDelegateToUserService() {
        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();
        dto.setUsername("john");

        trainerService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    @Test
    void updateTrainer_shouldUpdateTrainer_andReturnDTO() {
        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setIsActive(true);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainingTypeId(1L);

        UpdateTrainerRequestDTO dto = new UpdateTrainerRequestDTO();
        dto.setUsername("john");
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setTrainingTypeId(5L);
        dto.setIsActive(false);

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        ApiResponse<TrainerDTO> response =
                trainerService.updateTrainer(dto);

        assertFalse(user.getIsActive());
        assertEquals("New", user.getFirstName());
        assertEquals(5L, trainer.getTrainingTypeId());

        assertNotNull(response);
        assertFalse(response.getIsError());

        verify(trainerRepository).save(trainer);
    }

    @Test
    void changeStatusTrainee_shouldReturnStatusFromUserService() {
        ChangeStatusRequestDTO dto = new ChangeStatusRequestDTO();
        dto.setUsername("john");
        dto.setIsActive(false);

        when(userService.changeStatus(dto))
                .thenReturn(false);

        ApiResponse<?> response =
                trainerService.changeStatusTrainee(dto);

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertEquals(false, response.getData());

        verify(userService).changeStatus(dto);
    }

    @Test
    void getTrainersNotAssignedOnTrainee_shouldReturnDTOList() {
        User user = new User();
        user.setUsername("trainer1");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        trainer.setTrainingType(trainingType);
        when(trainerRepository.findTrainersNotAssignedToTrainee("john"))
                .thenReturn(List.of(trainer));

        ApiResponse<List<TrainerDTO>> response =
                trainerService.getTrainersNotAssignedOnTrainee("john");

        assertEquals(1, response.getData().size());
        assertEquals("trainer1",
                response.getData().getFirst().getUser().getUsername());

        verify(trainerRepository)
                .findTrainersNotAssignedToTrainee("john");
    }


    @Test
    void getTrainerEntityByUsername_shouldReturnTrainer() {
        Trainer trainer = new Trainer();

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        Trainer result =
                trainerService.getTrainerEntityByUsername("john");

        assertNotNull(result);
    }

    @Test
    void getTrainerEntityByUsername_shouldThrowException() {
        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> trainerService.getTrainerEntityByUsername("john"));
    }

    @Test
    void getTrainersByUsernames_shouldReturnList() {
        List<String> usernames = List.of("john", "alex");

        when(trainerRepository.findByUserUsernameIn(usernames))
                .thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> result =
                trainerService.getTrainersByUsernames(usernames);

        assertEquals(2, result.size());
    }
}