package com.epam.gym.service;

import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainer;
import com.epam.gym.util.UsernamePasswordGenerator;
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
    private UsernamePasswordGenerator generator;

    @InjectMocks
    private TrainerService trainerService;


    @Test
    void create_ShouldGenerateFieldsAndSaveTrainer() {

        Trainer trainer = new Trainer();
        trainer.setFirstName("David");
        trainer.setLastName("Wilson");

        when(trainerDao.findAll()).thenReturn(Collections.emptyList());
        when(generator.generateUsername("David", "Wilson", Collections.emptyList()))
                .thenReturn("David.Wilson");
        when(generator.generatePassword()).thenReturn("password123");

        Trainer result = trainerService.create(trainer);

        assertNotNull(result.getId());
        assertEquals("David.Wilson", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.isActive());

        verify(trainerDao).findAll();
        verify(generator).generateUsername("David", "Wilson", Collections.emptyList());
        verify(generator).generatePassword();
        verify(trainerDao).save(trainer);
        verifyNoMoreInteractions(trainerDao, generator);
    }


    @Test
    void find_ShouldReturnTrainer_WhenExists() {

        Trainer trainer = new Trainer();
        trainer.setId("TR1");

        when(trainerDao.findById("TR1")).thenReturn(trainer);

        Trainer result = trainerService.find("TR1");

        assertNotNull(result);
        assertEquals("TR1", result.getId());

        verify(trainerDao).findById("TR1");
        verifyNoMoreInteractions(trainerDao);
    }

    @Test
    void find_ShouldReturnNull_WhenNotFound() {

        when(trainerDao.findById("99")).thenReturn(null);

        Trainer result = trainerService.find("99");

        assertNull(result);

        verify(trainerDao).findById("99");
        verifyNoMoreInteractions(trainerDao);
    }


    @Test
    void update_ShouldCallSaveOnDao() {

        Trainer trainer = new Trainer();
        trainer.setUsername("Emma.Thompson");

        trainerService.update(trainer);

        verify(trainerDao).save(trainer);
        verifyNoMoreInteractions(trainerDao);
    }
}