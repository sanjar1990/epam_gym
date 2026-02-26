package com.epam.gym.mapper.training_type;

import com.epam.gym.dto.TrainingTypeDTO;
import com.epam.gym.entity.TrainingType;

public class TrainingTypeMapper {
    public static TrainingTypeDTO toTrainingTypeDTO(TrainingType trainingType) {
        TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
        trainingTypeDTO.setId(trainingType.getId());
        trainingTypeDTO.setTrainingTypeName(trainingType.getTrainingTypeName());
        return trainingTypeDTO;
    }

}
