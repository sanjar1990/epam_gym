package com.epam.gym.service;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.entity.TrainingType;
import com.epam.gym.enums.TrainingTypeEnum;
import com.epam.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void getAllTrainingTypes_shouldReturnMappedDTOList() {

        // ---------- Arrange ----------
        TrainingType type1 = new TrainingType();
        type1.setId(1L);
        type1.setTrainingTypeName(TrainingTypeEnum.CARDIO);

        TrainingType type2 = new TrainingType();
        type2.setId(2L);
        type2.setTrainingTypeName(TrainingTypeEnum.STRENGTH);

        when(trainingTypeRepository.findAll())
                .thenReturn(List.of(type1, type2));

        // ---------- Act ----------
        List<TrainingTypeDTO> result =
                trainingTypeService.getAllTrainingTypes();

        // ---------- Assert ----------
        assertEquals(2, result.size());
        assertEquals(TrainingTypeEnum.CARDIO, result.get(0).getTrainingTypeName());
        assertEquals(TrainingTypeEnum.STRENGTH, result.get(1).getTrainingTypeName());

        verify(trainingTypeRepository).findAll();
    }

    @Test
    void getAllTrainingTypes_shouldReturnEmptyList_whenRepositoryEmpty() {

        when(trainingTypeRepository.findAll())
                .thenReturn(List.of());

        List<TrainingTypeDTO> result =
                trainingTypeService.getAllTrainingTypes();

        assertTrue(result.isEmpty());
        verify(trainingTypeRepository).findAll();
    }
}