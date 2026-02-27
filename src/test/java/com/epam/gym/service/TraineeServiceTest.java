package com.epam.gym.service;

import com.epam.gym.dto.CreateTraineeCreateRequestDTO;
import com.epam.gym.dto.UpdateTraineeRequestDTO;
import com.epam.gym.dto.UserChangePasswordDTO;
import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TraineeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TraineeService traineeService;

    // --------------------------------
    // create()
    // --------------------------------

    @Test
    void create_shouldSaveTrainee() {
        CreateTraineeCreateRequestDTO dto = new CreateTraineeCreateRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("Seoul");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userService.generateUsername("John", "Doe"))
                .thenReturn("John.Doe");

        when(userService.generatePassword())
                .thenReturn("password123");

        traineeService.create(dto);

        verify(traineeRepository, times(1)).save(any(Trainee.class));
    }

    // --------------------------------
    // getTraineeByUsername()
    // --------------------------------

    @Test
    void getTraineeByUsername_shouldReturnTrainee_whenExists() {
        Trainee trainee = new Trainee();

        when(traineeRepository.findByUsername("john"))
                .thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getTraineeByUsername("john");

        assertNotNull(result);
    }

    @Test
    void getTraineeByUsername_shouldThrowException_whenNotFound() {
        when(traineeRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> traineeService.getTraineeByUsername("john"));
    }

    // --------------------------------
    // changePassword()
    // --------------------------------

    @Test
    void changePassword_shouldDelegateToUserService() {
        UserChangePasswordDTO dto = new UserChangePasswordDTO();

        traineeService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    // --------------------------------
    // updateTrainee()
    // --------------------------------

    @Test
    void updateTrainee_shouldUpdateAndSave() {
        String username = "john";
        String password = "123";

        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        UpdateTraineeRequestDTO dto = new UpdateTraineeRequestDTO();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setAddress("Busan");
        dto.setDateOfBirth(LocalDate.of(1995, 5, 5));

        when(authService.login(username, password)).thenReturn(user);
        when(traineeRepository.findByUsername(username))
                .thenReturn(Optional.of(trainee));

        boolean result = traineeService.updateTrainee(username, password, dto);

        assertTrue(result);
        assertEquals("New", trainee.getUser().getFirstName());
        assertEquals("Busan", trainee.getAddress());
        verify(traineeRepository).save(trainee);
    }

    // --------------------------------
    // activateDeactivateTrainer()
    // --------------------------------

    @Test
    void activateDeactivateTrainer_shouldCallChangeStatus() {
        String username = "john";
        String password = "123";

        User user = new User();
        user.setIsActive(true);

        when(authService.login(username, password)).thenReturn(user);
        when(userService.changeStatus(user)).thenReturn(false);

        boolean result = traineeService.activateDeactivateTrainer(username, password);

        assertFalse(result);
        verify(userService).changeStatus(user);
    }

    // --------------------------------
    // deleteTrainee()
    // --------------------------------

    @Test
    void deleteTrainee_shouldDeleteTrainee() {
        String username = "john";
        String password = "123";

        User user = new User();
        Trainee trainee = new Trainee();

        when(authService.login(username, password)).thenReturn(user);
        when(traineeRepository.findByUsername(username))
                .thenReturn(Optional.of(trainee));

        boolean result = traineeService.deleteTrainee(username, password);

        assertTrue(result);
        verify(traineeRepository).delete(trainee);
    }
}