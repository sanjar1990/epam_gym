package com.epam.gym.service;

import com.epam.gym.dao.TrainingDao;
import com.epam.gym.entity.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void create_ShouldGenerateIdAndSaveTraining() {

        Training training = new Training();

        trainingService.create(training);

        assertNotNull(training.getId());
        verify(trainingDao).save(training);
        verifyNoMoreInteractions(trainingDao);
    }

    @Test
    void find_ShouldReturnTraining_WhenExists() {

        Training training = new Training();
        training.setId(123L);

        when(trainingDao.findById(123L)).thenReturn(training);

        Training result = trainingService.find(123L);

        assertNotNull(result);
        assertEquals(123L, result.getId());

        verify(trainingDao).findById(123L);
        verifyNoMoreInteractions(trainingDao);
    }

    @Test
    void find_ShouldReturnNull_WhenTrainingNotFound() {

        when(trainingDao.findById(999L)).thenReturn(null);

        Training result = trainingService.find(999L);

        assertNull(result);

        verify(trainingDao).findById(999L);
        verifyNoMoreInteractions(trainingDao);
    }
}