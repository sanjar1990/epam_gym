package com.epam.gym.service;

import com.epam.gym.dto.CreateTrainerRequestDTO;
import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.dto.UpdateTrainerRequestDTO;
import com.epam.gym.dto.UserChangePasswordDTO;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.repository.TrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private TrainerAndTraineeService trainerAndTraineeService;

    @InjectMocks
    private TrainerService trainerService;

    // --------------------------------
    // create()
    // --------------------------------

    @Test
    void create_shouldSaveTrainer() {
        CreateTrainerRequestDTO dto = new CreateTrainerRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Smith");
        dto.setTrainingTypeId(1L);

        when(userService.generateUsername("John", "Smith"))
                .thenReturn("John.Smith");

        when(userService.generatePassword())
                .thenReturn("password123");

        trainerService.create(dto);

        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    // --------------------------------
    // getTrainerByUsername()
    // --------------------------------

    @Test
    void getTrainerByUsername_shouldReturnTrainer_whenExists() {
        Trainer trainer = new Trainer();

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getTrainerByUsername("john");

        assertNotNull(result);
    }

    @Test
    void getTrainerByUsername_shouldThrowException_whenNotFound() {
        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> trainerService.getTrainerByUsername("john"));
    }

    // --------------------------------
    // changePassword()
    // --------------------------------

    @Test
    void changePassword_shouldDelegateToUserService() {
        UserChangePasswordDTO dto = new UserChangePasswordDTO();
        dto.setUsername("john");

        trainerService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    // --------------------------------
    // updateTrainer()
    // --------------------------------

    @Test
    void updateTrainer_shouldAuthenticateUpdateAndSave() {
        String username = "john";
        String password = "123";

        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        UpdateTrainerRequestDTO dto = new UpdateTrainerRequestDTO();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setTrainingTypeId(5L);

        when(authService.login(username, password))
                .thenReturn(user);

        when(trainerRepository.findByUserUsername(username))
                .thenReturn(Optional.of(trainer));

        boolean result = trainerService.updateTrainer(username, password, dto);

        assertTrue(result);
        assertEquals("New", trainer.getUser().getFirstName());
        assertEquals(5L, trainer.getTrainingTypeId());
        verify(trainerRepository).save(trainer);
    }

    // --------------------------------
    // activateDeactivateTrainer()
    // --------------------------------

    @Test
    void activateDeactivateTrainer_shouldCallChangeStatus() {
        String username = "john";
        String password = "123";

        User user = new User();
        user.setUsername("john");

        when(authService.login(username, password))
                .thenReturn(user);

        when(userService.changeStatus(user))
                .thenReturn(false);

        boolean result = trainerService.activateDeactivateTrainer(username, password);

        assertFalse(result);
        verify(userService).changeStatus(user);
    }

    // --------------------------------
    // getTrainersNotAssignedOnTrainee()
    // --------------------------------

    @Test
    void getTrainersNotAssignedOnTrainee_shouldReturnList() {
        List<TrainerDTO> list = List.of(new TrainerDTO());

        when(trainerAndTraineeService
                .getTrainersNotAssignedOnTrainee("trainee1"))
                .thenReturn(list);

        List<TrainerDTO> result =
                trainerService.getTrainersNotAssignedOnTrainee("trainee1");

        assertEquals(1, result.size());
        verify(trainerAndTraineeService)
                .getTrainersNotAssignedOnTrainee("trainee1");
    }
}