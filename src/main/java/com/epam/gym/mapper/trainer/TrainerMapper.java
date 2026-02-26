package com.epam.gym.mapper.trainer;

import com.epam.gym.dto.TrainerDTO;
import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.dto.UserDTO;
import com.epam.gym.entity.Trainer;
import com.epam.gym.enums.TrainingTypeEnum;
import com.epam.gym.mapper.TrainerMapperI;
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
    public static TrainerDTO toTrainerDTO(TrainerMapperI mapper){
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setId(mapper.getTrainerId());
        UserDTO user = new UserDTO();
        user.setFirstName(mapper.getFirstName());
        user.setLastName(mapper.getLastName());
        user.setUsername(mapper.getTrainerUsername());
        user.setIsActive(mapper.getIsActive());
        trainerDTO.setUser(user);
        TrainingTypeDTO trainingType = new TrainingTypeDTO();
        trainingType.setId(mapper.getTrainingTypeId());
        trainingType.setTrainingTypeName(TrainingTypeEnum.valueOf(mapper.getTrainingTypeName()));
        trainerDTO.setTrainingType(trainingType);
        return trainerDTO;
    }
}
