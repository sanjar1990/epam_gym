package com.epam.gym.service;

import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.entity.TrainerAndTrainee;
import com.epam.gym.mapper.TrainerMapperI;
import com.epam.gym.repository.TrainerAndTraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerAndTraineeServiceTest {

    @Mock
    private TrainerAndTraineeRepository trainerAndTraineeRepository;

    @InjectMocks
    private TrainerAndTraineeService trainerAndTraineeService;

    // --------------------------------
    // addTrainerAndTrainee()
    // --------------------------------

    @Test
    void addTrainerAndTrainee_shouldSaveEntity() {
        Long trainerId = 1L;
        Long traineeId = 2L;

        trainerAndTraineeService.addTrainerAndTrainee(trainerId, traineeId);

        verify(trainerAndTraineeRepository, times(1))
                .save(any(TrainerAndTrainee.class));
    }

    // --------------------------------
    // getTrainersNotAssignedOnTrainee()
    // --------------------------------

    @Test
    void getTrainersNotAssignedOnTrainee_shouldReturnMappedDTOList() {
        String username = "trainee1";

        TrainerMapperI projection = mock(TrainerMapperI.class);

        when(projection.getFirstName()).thenReturn("John");
        when(projection.getLastName()).thenReturn("Smith");
        when(projection.getTrainingTypeName()).thenReturn("CARDIO");
        // VERY IMPORTANT — must match enum constant exactly

        when(trainerAndTraineeRepository
                .findTrainersNotAssignedOnTrainee(username))
                .thenReturn(List.of(projection));

        List<TrainerDTO> result =
                trainerAndTraineeService
                        .getTrainersNotAssignedOnTrainee(username);

        assertEquals(1, result.size());
    }
}