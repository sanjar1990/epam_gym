package com.epam.gym.service;
import com.epam.gym.dao.TraineeDao;
import com.epam.gym.dao.TrainerDao;
import com.epam.gym.entity.Trainee;
import com.epam.gym.util.PasswordGenerator;
import com.epam.gym.util.UsernameGenerator;
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
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UsernameGenerator usernameGenerator;
    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void create_ShouldGenerateFieldsAndSave() {

        // given
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
        when(usernameGenerator.generateUsername("John", "Smith", Collections.emptyList()))
                .thenReturn("John.Smith");
        when(passwordGenerator.generatePassword()).thenReturn("1234567890");

        // when
        Trainee result = traineeService.create(trainee);

        // then
        assertNotNull(result.getId());
        assertEquals("John.Smith", result.getUsername());
        assertEquals("1234567890", result.getPassword());
        assertTrue(result.isActive());

        verify(traineeDao).findAll();
        verify(usernameGenerator).generateUsername("John", "Smith", Collections.emptyList());
        verify(passwordGenerator).generatePassword();
        verify(traineeDao).save(trainee);
        verifyNoMoreInteractions(traineeDao, usernameGenerator, passwordGenerator);
    }

    @Test
    void find_ShouldReturnTrainee() {

        Trainee trainee = new Trainee();
        trainee.setId(1L);

        when(traineeDao.findById(1L)).thenReturn(trainee);

        Trainee result = traineeService.find(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(traineeDao).findById(1L);
        verifyNoMoreInteractions(traineeDao);
    }

    @Test
    void find_ShouldReturnNull_WhenNotFound() {

        when(traineeDao.findById(99L)).thenReturn(null);

        Trainee result = traineeService.find(99L);

        assertNull(result);

        verify(traineeDao).findById(99L);
        verifyNoMoreInteractions(traineeDao);
    }

    @Test
    void update_ShouldCallUpdate() {

        Trainee trainee = new Trainee();
        trainee.setUsername("updatedUser");

        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        traineeService.update(trainee);

        verify(traineeDao).findAll();
        verify(trainerDao).findAll();
        verify(traineeDao).update(eq(trainee), anyCollection());
        verifyNoMoreInteractions(traineeDao, trainerDao);
    }

    // TODO:
    //  This test fails
    @Test
    void delete_ShouldRemoveFromStorage() {
        assertDoesNotThrow(() -> traineeService.delete(1L));
    }
}
