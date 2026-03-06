package com.epam.gym.mapper.trainer;

import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.entity.Trainer;
import com.epam.gym.mapper.training_type.TrainingTypeMapper;
import com.epam.gym.mapper.user.UserMapper;

public class TrainerMapper {
    public static TrainerDTO toTrainerDTO(Trainer trainer) {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setId(trainer.getId());
        trainerDTO.setUser(UserMapper.toUserDTO(trainer.getUser()));
        trainerDTO.setTrainingType(TrainingTypeMapper
                .toTrainingTypeDTO(trainer.getTrainingType()));
        return trainerDTO;
    }

}
