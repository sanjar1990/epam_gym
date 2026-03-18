package com.epam.gym.service;

import com.epam.gym.dto.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingService trainingService;


    @Test
    void addTraining_shouldSaveTraining_whenValid() {

        // ---------- Arrange ----------
        CreateTrainingDTO dto = new CreateTrainingDTO();
        dto.setTraineeUsername("john");
        dto.setTrainerUsername("mike");
        dto.setTrainingTypeId(5L);
        dto.setTrainingDate(LocalDate.now());
        dto.setTrainingDuration(60);

        // Trainee
        Trainee trainee = new Trainee();
        trainee.setTrainings(new ArrayList<>());
        trainee.setTrainers(new HashSet<>());

        // TrainingType
        TrainingType trainingType = new TrainingType();
        trainingType.setId(5L);

        // Trainer
        Trainer trainer = new Trainer();
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new HashSet<>());

        when(traineeService.getTrainee("john")).thenReturn(trainee);
        when(trainerService.getTrainerEntityByUsername("mike")).thenReturn(trainer);

        ApiResponse<?> response = trainingService.addTraining(dto);


        assertNotNull(response);
        assertFalse(response.getIsError());

        verify(trainingRepository).save(any(Training.class));
        assertEquals(1, trainee.getTrainings().size());
        assertEquals(1, trainee.getTrainers().size());
    }

    @Test
    void addTraining_shouldThrowException_whenTrainingTypeMismatch() {

        CreateTrainingDTO dto = new CreateTrainingDTO();
        dto.setTraineeUsername("john");
        dto.setTrainerUsername("mike");
        dto.setTrainingTypeId(99L); // mismatch

        Trainee trainee = new Trainee();
        trainee.setTrainings(new ArrayList<>());
        trainee.setTrainers(new HashSet<>());

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setTrainingType(trainingType);
        trainer.setTrainees(new HashSet<>());

        when(traineeService.getTrainee("john")).thenReturn(trainee);
        when(trainerService.getTrainerEntityByUsername("mike")).thenReturn(trainer);

        assertThrows(RuntimeException.class,
                () -> trainingService.addTraining(dto));
    }


    @Test
    void getTrainingsByTraineeUsernameCriteria_shouldReturnList() {

        GetTraineeTrainingsCriteriaFilterDTO dto =
                new GetTraineeTrainingsCriteriaFilterDTO();

        Training training = buildFullTraining();

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(training));

        ApiResponse<List<TraineeTrainingResponseDTO>> response =
                trainingService.getTrainingsByTraineeUsernameCriteria(dto);

        List<TraineeTrainingResponseDTO> result = response.getData();

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertEquals(1, result.size());

        verify(trainingRepository).findAll(any(Specification.class));
    }

    @Test
    void getTrainingsByTrainerUsernameCriteria_shouldReturnList() {

        GetTrainerTrainingsCriteriaFilterDTO dto =
                new GetTrainerTrainingsCriteriaFilterDTO();

        Training training = buildFullTraining();

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(training));

        ApiResponse<List<TrainerTrainingResponseDTO>> response =
                trainingService.getTrainingsByTrainerUsernameCriteria(dto);

        List<TrainerTrainingResponseDTO> result = response.getData();

        assertNotNull(response);
        assertFalse(response.getIsError());
        assertEquals(1, result.size());

        verify(trainingRepository).findAll(any(Specification.class));
    }

    private Training buildFullTraining() {

        User traineeUser = new User();
        traineeUser.setUsername("john");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername("mike");

        TrainingType type = new TrainingType();
        type.setId(5L);
        type.setTrainingTypeName(TrainingTypeEnum.CARDIO);

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setTrainingType(type);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(type);
        training.setTrainingDate(LocalDate.now());
        training.setTrainingDuration(60);
        return training;
    }
}