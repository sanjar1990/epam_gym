package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.TrainingType;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void createTrainee_shouldSaveTrainee_andReturnAuthDTO() {
        CreateTraineeRequestDTO dto = new CreateTraineeRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("Seoul");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userService.generateUsername("John", "Doe"))
                .thenReturn("john.doe");

        when(userService.generatePassword())
                .thenReturn("pass123");

        ApiResponse<AuthDTO> response =
                traineeService.createTrainee(dto);

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertEquals("john.doe", response.getData().getUsername());

        verify(traineeRepository).save(any(Trainee.class));
    }


    @Test
    void getTraineeByUsername_shouldReturnDTO_whenExists() {
        User user = new User();
        user.setUsername("john");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainee));

        ApiResponse<TraineeDTO> response =
                traineeService.getTraineeByUsername("john");

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertNotNull(response.getData());

        verify(traineeRepository).findByUserUsername("john");
    }

    @Test
    void getTraineeByUsername_shouldThrowException_whenNotFound() {
        when(traineeRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> traineeService.getTraineeByUsername("john"));
    }

    @Test
    void changePassword_shouldDelegateToUserService() {
        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();

        traineeService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    @Test
    void updateTrainee_shouldUpdateAndSave() {
        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setIsActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        UpdateTraineeRequestDTO dto = new UpdateTraineeRequestDTO();
        dto.setUsername("john");
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setAddress("Busan");
        dto.setDateOfBirth(LocalDate.of(1995, 5, 5));
        dto.setIsActive(false);

        when(traineeRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainee));

        when(traineeRepository.save(any(Trainee.class)))
                .thenReturn(trainee);

        ApiResponse<TraineeDTO> response =
                traineeService.updateTrainee(dto);

        assertEquals("New", user.getFirstName());
        assertEquals("Busan", trainee.getAddress());
        assertFalse(user.getIsActive());

        assertNotNull(response);
        assertFalse(response.getIsError());

        verify(traineeRepository).save(trainee);
    }

    @Test
    void changeStatusTrainee_shouldReturnStatus() {
        ChangeStatusRequestDTO dto = new ChangeStatusRequestDTO();
        dto.setUsername("john");
        dto.setIsActive(false);

        when(userService.changeStatus(dto))
                .thenReturn(false);

        ApiResponse<?> response =
                traineeService.changeStatusTrainee(dto);

        assertFalse(response.getIsError());
        assertEquals(false, response.getData());

        verify(userService).changeStatus(dto);
    }

    @Test
    void deleteTrainee_shouldDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());

        when(traineeRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainee));

        ApiResponse<?> response =
                traineeService.deleteTrainee("john");

        assertNotNull(response);
        assertFalse(response.getIsError());

        verify(traineeRepository).delete(trainee);
    }

    @Test
    void updateTrainerList_shouldReplaceTrainerList() {
        User user = new User();
        user.setUsername("trainer1");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        trainer.setTrainingType(trainingType);
        Trainee trainee = new Trainee();
        trainee.setTrainers(new HashSet<>());

        UpdateTrainersRequestDTO dto = new UpdateTrainersRequestDTO();
        dto.setTraineeUsername("john");
        dto.setTrainerUsernames(List.of("trainer1"));

        when(traineeRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainee));

        when(trainerService.getTrainersByUsernames(List.of("trainer1")))
                .thenReturn(List.of(trainer));

        ApiResponse<List<TrainerDTO>> response =
                traineeService.updateTrainerList(dto);

        assertEquals(1, trainee.getTrainers().size());
        assert response != null;
        assertFalse(response.getIsError());

        verify(traineeRepository).save(trainee);
    }
}