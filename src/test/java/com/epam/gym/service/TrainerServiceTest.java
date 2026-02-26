//package com.epam.gym.service;
//
//import com.epam.gym.repository.TraineeRepository;
//import com.epam.gym.repository.TrainerRepository;
//import com.epam.gym.entity.Trainer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrainerServiceTest {
//
//    @Mock
//    private TrainerRepository trainerRepository;
//    @Mock
//    private TraineeRepository traineeDao;
//
//    @Mock
//    private UsernameGenerator usernameGenerator;
//    @Mock
//    private PasswordGenerator passwordGenerator;
//
//
//    @InjectMocks
//    private TrainerService trainerService;
//
//
//    @Test
//    void create_ShouldGenerateFieldsAndSaveTrainer() {
//
//        Trainer trainer = new Trainer();
//        trainer.setFirstName("David");
//        trainer.setLastName("Wilson");
//
//        when(trainerRepository.findAll()).thenReturn(Collections.emptyList());
//        when(usernameGenerator.generateUsername("David", "Wilson", Collections.emptyList()))
//                .thenReturn("David.Wilson");
//        when(passwordGenerator.generatePassword()).thenReturn("password123");
//
//        Trainer result = trainerService.create(trainer);
//
//        assertNotNull(result.getId());
//        assertEquals("David.Wilson", result.getUsername());
//        assertEquals("password123", result.getPassword());
//        assertTrue(result.isActive());
//
//        verify(trainerRepository).findAll();
//        verify(usernameGenerator).generateUsername("David", "Wilson", Collections.emptyList());
//        verify(passwordGenerator).generatePassword();
//        verify(trainerRepository).save(trainer);
//        verifyNoMoreInteractions(trainerRepository, usernameGenerator, passwordGenerator);
//    }
//
//
//    @Test
//    void find_ShouldReturnTrainer_WhenExists() {
//
//        Trainer trainer = new Trainer();
//        trainer.setId(1L);
//
//        when(trainerRepository.findById(1L)).thenReturn(trainer);
//
//        Trainer result = trainerService.find(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//
//        verify(trainerRepository).findById(1L);
//        verifyNoMoreInteractions(trainerRepository);
//    }
//
//    @Test
//    void find_ShouldReturnNull_WhenNotFound() {
//
//        when(trainerRepository.findById(99L)).thenReturn(null);
//
//        Trainer result = trainerService.find(99L);
//
//        assertNull(result);
//
//        verify(trainerRepository).findById(99L);
//        verifyNoMoreInteractions(trainerRepository);
//    }
//
//
//    @Test
//    void update_ShouldCallSaveOnDao() {
//
//        Trainer trainer = new Trainer();
//        trainer.setUsername("Emma.Thompson");
//        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
//        when(trainerRepository.findAll()).thenReturn(Collections.emptyList());
//
//        trainerService.update(trainer);
//        verify(traineeDao).findAll();
//        verify(trainerRepository).findAll();
//        verify(trainerRepository).update(eq(trainer), anyCollection());
//        verifyNoMoreInteractions(trainerRepository);
//    }
//}