package com.epam.gym.service;
import com.epam.gym.dao.TraineeDao;
import com.epam.gym.entity.Trainee;
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
    private UsernamePasswordGenerator generator;

    @InjectMocks
    private TraineeService traineeService;

    @Test
    void create_ShouldGenerateFieldsAndSave() {

        // given
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Smith");

        when(traineeDao.findAll()).thenReturn(Collections.emptyList());
        when(generator.generateUsername("John", "Smith", Collections.emptyList()))
                .thenReturn("John.Smith");
        when(generator.generatePassword()).thenReturn("1234567890");

        // when
        Trainee result = traineeService.create(trainee);

        // then
        assertNotNull(result.getId());
        assertEquals("John.Smith", result.getUsername());
        assertEquals("1234567890", result.getPassword());
        assertTrue(result.isActive());

        verify(traineeDao).findAll();
        verify(generator).generateUsername("John", "Smith", Collections.emptyList());
        verify(generator).generatePassword();
        verify(traineeDao).save(trainee);
        verifyNoMoreInteractions(traineeDao, generator);
    }

    @Test
    void find_ShouldReturnTrainee() {

        Trainee trainee = new Trainee();
        trainee.setId("1");

        when(traineeDao.findById("1")).thenReturn(trainee);

        Trainee result = traineeService.find("1");

        assertNotNull(result);
        assertEquals("1", result.getId());

        verify(traineeDao).findById("1");
        verifyNoMoreInteractions(traineeDao);
    }

    @Test
    void find_ShouldReturnNull_WhenNotFound() {

        when(traineeDao.findById("99")).thenReturn(null);

        Trainee result = traineeService.find("99");

        assertNull(result);

        verify(traineeDao).findById("99");
        verifyNoMoreInteractions(traineeDao);
    }

    @Test
    void update_ShouldCallSave() {

        Trainee trainee = new Trainee();
        trainee.setUsername("updatedUser");

        traineeService.update(trainee);

        verify(traineeDao).save(trainee);
        verifyNoMoreInteractions(traineeDao);
    }

    // TODO:
    //  This test fails
    @Test
    void delete_ShouldRemoveFromStorage() {


        assertDoesNotThrow(() -> traineeService.delete("1"));
    }
}
