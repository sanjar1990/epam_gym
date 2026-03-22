package com.epam.gym.service;

import com.epam.gym.dto.*;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.TrainingType;
import com.epam.gym.entity.User;
import com.epam.gym.exceptions.UserNotFoundException;
import com.epam.gym.mapper.trainer.TrainerMapperI;
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
    private TrainerMapperI trainerMapper;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void createTrainer_shouldCreateTrainer_andReturnAuthDTO() {
        CreateTrainerRequestDTO dto = new CreateTrainerRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Smith");
        dto.setTrainingTypeId(1L);

        when(userService.generateUsername("John", "Smith"))
                .thenReturn("john.smith");

        when(userService.generatePassword())
                .thenReturn("pass123");

        when(trainerMapper.toEntity(any()))
                .thenAnswer(invocation -> {
                    Trainer t = new Trainer();
                    t.setUser(new User());
                    return t;
                });

        AuthDTO response = trainerService.createTrainer(dto);

        assertNotNull(response);
        assertEquals("john.smith", response.getUsername());
        assertEquals("pass123", response.getPassword());

        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void getTrainerByUsername_shouldReturnDTO_whenExists() {
        User user = new User();
        user.setUsername("john");

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));


        when(trainerMapper.toTrainerDTO(any(Trainer.class)))
                .thenReturn(new TrainerDTO());

        TrainerDTO response =
                trainerService.getTrainerByUsername("john");

        assertNotNull(response);

        verify(trainerRepository).findByUserUsername("john");
    }

    @Test
    void getTrainerByUsername_shouldThrowException_whenNotFound() {
        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> trainerService.getTrainerByUsername("john"));
    }

    @Test
    void changePassword_shouldDelegateToUserService() {
        UserChangePasswordRequestDTO dto = new UserChangePasswordRequestDTO();
        dto.setUsername("john");

        trainerService.changePassword(dto);

        verify(userService).changePassword(dto);
    }

    @Test
    void updateTrainer_shouldUpdateTrainer_andReturnDTO() {
        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setIsActive(true);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);
        trainer.setTrainingTypeId(1L);

        UpdateTrainerRequestDTO dto = new UpdateTrainerRequestDTO();
        dto.setUsername("john");
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setTrainingTypeId(5L);
        dto.setIsActive(false);

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        when(trainerRepository.save(any(Trainer.class)))
                .thenReturn(trainer);

        doAnswer(invocation -> {
            UpdateTrainerRequestDTO d = invocation.getArgument(0);
            Trainer t = invocation.getArgument(1);

            t.getUser().setFirstName(d.getFirstName());
            t.getUser().setLastName(d.getLastName());
            t.getUser().setIsActive(d.getIsActive());
            t.setTrainingTypeId(d.getTrainingTypeId());

            return null;
        }).when(trainerMapper).updateTrainerFromDto(any(), any());

        when(trainerMapper.toTrainerDTO(any()))
                .thenReturn(new TrainerDTO());

        TrainerDTO response =
                trainerService.updateTrainer(dto);

        assertFalse(user.getIsActive());
        assertEquals("New", user.getFirstName());
        assertEquals(5L, trainer.getTrainingTypeId());

        assertNotNull(response);

        verify(trainerRepository).save(trainer);
    }

    @Test
    void changeStatusTrainee_shouldDelegateToUserService() {
        ChangeStatusRequestDTO dto = new ChangeStatusRequestDTO();
        dto.setUsername("john");
        dto.setIsActive(false);

        trainerService.changeStatusTrainee(dto);

        verify(userService).changeStatus(dto);
    }

    @Test
    void getTrainersNotAssignedOnTrainee_shouldReturnDTOList() {
        User user = new User();
        user.setUsername("trainer1");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainer.setTrainingType(trainingType);

        when(trainerRepository.findTrainersNotAssignedToTrainee("john"))
                .thenReturn(List.of(trainer));

        List<TrainerDTO> response =
                trainerService.getTrainersNotAssignedOnTrainee("john");

        assertEquals(1, response.size());

        verify(trainerRepository)
                .findTrainersNotAssignedToTrainee("john");
    }

    @Test
    void getTrainerEntityByUsername_shouldReturnTrainer() {
        Trainer trainer = new Trainer();

        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.of(trainer));

        Trainer result =
                trainerService.getTrainerEntityByUsername("john");

        assertNotNull(result);
    }

    @Test
    void getTrainerEntityByUsername_shouldThrowException() {
        when(trainerRepository.findByUserUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> trainerService.getTrainerEntityByUsername("john"));
    }

    @Test
    void getTrainersByUsernames_shouldReturnList() {
        List<String> usernames = List.of("john", "alex");

        when(trainerRepository.findByUserUsernameIn(usernames))
                .thenReturn(List.of(new Trainer(), new Trainer()));

        List<Trainer> result =
                trainerService.getTrainersByUsernames(usernames);

        assertEquals(2, result.size());
    }
}