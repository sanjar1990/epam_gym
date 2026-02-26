package com.epam.gym.mapper.trainee;

import com.epam.gym.dto.TraineeDTO;
import com.epam.gym.entity.Trainee;
import com.epam.gym.mapper.user.UserMapper;

public class TraineeMapper {
    public static TraineeDTO toTraineeDTO(Trainee trainee) {
        TraineeDTO dto = new TraineeDTO();
        dto.setId(trainee.getId());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setUser(UserMapper.toUserDTO(trainee.getUser()));
        return dto;
    }
}
