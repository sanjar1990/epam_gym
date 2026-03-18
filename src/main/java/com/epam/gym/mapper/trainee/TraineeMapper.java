package com.epam.gym.mapper.trainee;

import com.epam.gym.dto.TraineeDTO;
import com.epam.gym.entity.Trainee;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.mapper.user.UserMapper;

public class TraineeMapper {
    public static TraineeDTO toTraineeDTO(Trainee trainee) {
        TraineeDTO dto = new TraineeDTO();
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setUser(UserMapper.toUserDTO(trainee.getUser()));
        dto.setTrainersList(trainee.getTrainers().stream()
                .map(TrainerMapper::toTrainerDTO).toList());
        return dto;
    }
}
