package com.epam.gym.service;

import com.epam.gym.dto.CreateTrainingDTO;
import com.epam.gym.dto.GetTraineeTrainingsCriteriaFilterDTO;
import com.epam.gym.dto.GetTrainerTrainingsCriteriaFilterDTO;
import com.epam.gym.dto.TrainingResponseDTO;
import com.epam.gym.entity.*;
import com.epam.gym.enums.TrainingTypeEnum;
import com.epam.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private AuthService authService;

    @Mock
    private TraineeService traineeService;


    @InjectMocks
    private TrainingService trainingService;

    // =====================================================
    // Helper method to build FULL valid Training object
    // =====================================================

    private Training buildFullTraining() {

        // ---------- TRAINEE USER ----------
        User traineeUser = new User();
        traineeUser.setId(1L);
        traineeUser.setFirstName("John");
        traineeUser.setLastName("Doe");
        traineeUser.setUsername("john");

        // ---------- TRAINEE ----------
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(traineeUser);

        // ---------- TRAINER USER ----------
        User trainerUser = new User();
        trainerUser.setId(2L);
        trainerUser.setFirstName("Mike");
        trainerUser.setLastName("Smith");
        trainerUser.setUsername("mike");

        // ---------- TRAINING TYPE ----------
        TrainingType trainingType = new TrainingType();
        trainingType.setId(5L);
        trainingType.setTrainingTypeName(TrainingTypeEnum.CARDIO);

        // ---------- TRAINER ----------
        Trainer trainer = new Trainer();
        trainer.setId(10L);
        trainer.setUser(trainerUser);
        trainer.setTrainingType(trainingType);

        // ---------- TRAINING ----------
        Training training = new Training();
        training.setId(100L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);   // IMPORTANT
        training.setTrainingName("Cardio");
        training.setTrainingDuration(60);
        training.setTrainingDate(LocalDate.now());

        return training;
    }

    // =====================================================
    // addTraining()
    // =====================================================

    @Test
    void addTraining_shouldAuthenticateSaveAndAssignTrainer() {

        String username = "john";
        String password = "123";

        CreateTrainingDTO dto = new CreateTrainingDTO();
        dto.setTrainerId(10L);
        dto.setTrainingName("Cardio");
        dto.setTrainingTypeId(1L);
        dto.setTrainingDate(LocalDate.now());
        dto.setTrainingDuration(60);

        Trainee trainee = new Trainee();
        trainee.setId(5L);

        when(authService.login(username, password)).thenReturn(new User());
        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);

        trainingService.addTraining(username, password, dto);

        verify(trainingRepository).save(any(Training.class));

    }

    // =====================================================
    // getTrainingsByTraineeUsernameCriteria()
    // =====================================================

    @Test
    void getTrainingsByTraineeUsernameCriteria_shouldReturnList() {

        GetTraineeTrainingsCriteriaFilterDTO dto =
                new GetTraineeTrainingsCriteriaFilterDTO();

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(buildFullTraining()));

        List<TrainingResponseDTO> result =
                trainingService.getTrainingsByTraineeUsernameCriteria(dto);

        assertEquals(1, result.size());
        verify(trainingRepository).findAll(any(Specification.class));
    }

    // =====================================================
    // getTrainingsByTrainerUsernameCriteria()
    // =====================================================

    @Test
    void getTrainingsByTrainerUsernameCriteria_shouldReturnList() {

        GetTrainerTrainingsCriteriaFilterDTO dto =
                new GetTrainerTrainingsCriteriaFilterDTO();

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(buildFullTraining()));

        List<TrainingResponseDTO> result =
                trainingService.getTrainingsByTrainerUsernameCriteria(dto);

        assertEquals(1, result.size());
        verify(trainingRepository).findAll(any(Specification.class));
    }

    // =====================================================
    // updateTrainerList()
    // =====================================================

    @Test
    void updateTrainerList_shouldUpdateTrainerId() {

        String username = "john";
        String password = "123";
        Long trainerId = 20L;
        Long trainingId = 1L;

        Training training = new Training();
        training.setId(trainingId);

        when(authService.login(username, password)).thenReturn(new User());
        when(trainingRepository.findById(trainingId))
                .thenReturn(Optional.of(training));

        trainingService.updateTrainerList(
                username, password, trainerId, trainingId);

        assertEquals(trainerId, training.getTrainerId());
        verify(trainingRepository).save(training);
    }

    @Test
    void updateTrainerList_shouldThrowException_whenTrainingNotFound() {

        when(authService.login(any(), any())).thenReturn(new User());
        when(trainingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                trainingService.updateTrainerList(
                        "john", "123", 10L, 1L));
    }
}