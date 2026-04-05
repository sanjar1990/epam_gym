package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.*;
import com.epam.gym.mapper.training.TrainingMapperI;
import com.epam.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingMapperI trainingMapperI;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void addTraining_shouldSaveTraining_whenValid() {

        CreateTrainingDTO dto = new CreateTrainingDTO();
        dto.setTraineeUsername("john");
        dto.setTrainerUsername("mike");
        dto.setTrainingTypeId(5L);

        Trainee trainee = new Trainee();
        trainee.setTrainings(new ArrayList<>());
        trainee.setTrainers(new HashSet<>());

        TrainingType type = new TrainingType();
        type.setId(5L);

        Trainer trainer = new Trainer();
        trainer.setTrainingType(type);
        trainer.setTrainees(new HashSet<>());

        Training training = new Training();

        when(traineeService.getTrainee("john")).thenReturn(trainee);
        when(trainerService.getTrainerEntityByUsername("mike")).thenReturn(trainer);
        when(trainingMapperI.toEntity(dto)).thenReturn(training);

        trainingService.addTraining(dto);

        assertEquals(1, trainee.getTrainings().size());
        assertEquals(1, trainee.getTrainers().size());
        assertEquals(1, trainer.getTrainees().size());

        verify(trainingRepository).save(training);
    }

    @Test
    void addTraining_shouldThrowException_whenTrainingTypeMismatch() {

        CreateTrainingDTO dto = new CreateTrainingDTO();
        dto.setTraineeUsername("john");
        dto.setTrainerUsername("mike");
        dto.setTrainingTypeId(99L);

        Trainee trainee = new Trainee();
        trainee.setTrainings(new ArrayList<>());
        trainee.setTrainers(new HashSet<>());

        TrainingType type = new TrainingType();
        type.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setTrainingType(type);
        trainer.setTrainees(new HashSet<>());

        when(traineeService.getTrainee("john")).thenReturn(trainee);
        when(trainerService.getTrainerEntityByUsername("mike")).thenReturn(trainer);

        assertThrows(RuntimeException.class,
                () -> trainingService.addTraining(dto));

        verify(trainingRepository, never()).save(any());
    }

    @Test
    void getTrainingsByTraineeUsernameCriteria_shouldReturnList() {

        GetTraineeTrainingsCriteriaFilterDTO dto =
                new GetTraineeTrainingsCriteriaFilterDTO();

        Training training = buildFullTraining();

        when(trainingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(training));

        when(trainingMapperI.toTraineeTrainingResponseDTO(training))
                .thenReturn(new TraineeTrainingResponseDTO());

        List<TraineeTrainingResponseDTO> result =
                trainingService.getTrainingsByTraineeUsernameCriteria(dto);

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

        when(trainingMapperI.toTrainerTrainingResponseDTO(training))
                .thenReturn(new TrainerTrainingResponseDTO());

        List<TrainerTrainingResponseDTO> result =
                trainingService.getTrainingsByTrainerUsernameCriteria(dto);

        assertEquals(1, result.size());

        verify(trainingRepository).findAll(any(Specification.class));
    }

    @Test
    void getTrainingsCount_shouldReturnCount() {

        when(trainingRepository.count()).thenReturn(5L);

        Long result = trainingService.getTrainingsCount();

        assertEquals(5L, result);
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

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setTrainingType(type);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(type);

        return training;
    }
}