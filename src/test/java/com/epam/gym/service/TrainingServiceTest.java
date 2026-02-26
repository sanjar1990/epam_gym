//package com.epam.gym.service;
//
//import com.epam.gym.repository.TrainingRepository;
//import com.epam.gym.entity.Training;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainingServiceTest {
//
//    @Mock
//    private TrainingRepository trainingRepository;
//
//    @InjectMocks
//    private TrainingService trainingService;
//
//    @Test
//    void create_ShouldGenerateIdAndSaveTraining() {
//
//        Training training = new Training();
//
//        trainingService.create(training);
//
//        assertNotNull(training.getId());
//        verify(trainingRepository).save(training);
//        verifyNoMoreInteractions(trainingRepository);
//    }
//
//    @Test
//    void find_ShouldReturnTraining_WhenExists() {
//
//        Training training = new Training();
//        training.setId(123L);
//
//        when(trainingRepository.findById(123L)).thenReturn(training);
//
//        Training result = trainingService.find(123L);
//
//        assertNotNull(result);
//        assertEquals(123L, result.getId());
//
//        verify(trainingRepository).findById(123L);
//        verifyNoMoreInteractions(trainingRepository);
//    }
//
//    @Test
//    void find_ShouldReturnNull_WhenTrainingNotFound() {
//
//        when(trainingRepository.findById(999L)).thenReturn(null);
//
//        Training result = trainingService.find(999L);
//
//        assertNull(result);
//
//        verify(trainingRepository).findById(999L);
//        verifyNoMoreInteractions(trainingRepository);
//    }
//}