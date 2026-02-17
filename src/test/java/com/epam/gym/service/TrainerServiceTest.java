package com.epam.gym.service;

import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainer;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGenerator passwordGenerator;


    @InjectMocks
    private TrainerService trainerService;


    @Test
    void create_ShouldGenerateFieldsAndSaveTrainer() {

        Trainer trainer = new Trainer();
        trainer.setFirstName("David");
        trainer.setLastName("Wilson");

        when(trainerDao.findAll()).thenReturn(Collections.emptyList());
        when(usernameGenerator.generateUsername("David", "Wilson", Collections.emptyList()))
                .thenReturn("David.Wilson");
        when(passwordGenerator.generatePassword()).thenReturn("password123");

        Trainer result = trainerService.create(trainer);

        assertNotNull(result.getId());
        assertEquals("David.Wilson", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.isActive());

        verify(trainerDao).findAll();
        verify(usernameGenerator).generateUsername("David", "Wilson", Collections.emptyList());
        verify(passwordGenerator).generatePassword();
        verify(trainerDao).save(trainer);
        verifyNoMoreInteractions(trainerDao, usernameGenerator, passwordGenerator);
    }


    @Test
    void find_ShouldReturnTrainer_WhenExists() {

        Trainer trainer = new Trainer();
        trainer.setId(1L);

        when(trainerDao.findById(1L)).thenReturn(trainer);

        Trainer result = trainerService.find(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(trainerDao).findById(1L);
        verifyNoMoreInteractions(trainerDao);
    }

    @Test
    void find_ShouldReturnNull_WhenNotFound() {

        when(trainerDao.findById(99L)).thenReturn(null);

        Trainer result = trainerService.find(99L);

        assertNull(result);

        verify(trainerDao).findById(99L);
        verifyNoMoreInteractions(trainerDao);
    }


    @Test
    void update_ShouldCallSaveOnDao() {

        Trainer trainer = new Trainer();
        trainer.setUsername("Emma.Thompson");
        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        trainerService.update(trainer);
        verify(traineeDao).findAll();
        verify(trainerDao).findAll();
        verify(trainerDao).update(eq(trainer), anyCollection());
        verifyNoMoreInteractions(trainerDao);
    }
}