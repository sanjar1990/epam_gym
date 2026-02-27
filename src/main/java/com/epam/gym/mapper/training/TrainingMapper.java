package com.epam.gym.mapper.training;

import com.epam.gym.dto.TrainingResponseDTO;
import com.epam.gym.entity.Training;
import com.epam.gym.mapper.trainee.TraineeMapper;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.mapper.training_type.TrainingTypeMapper;

public class TrainingMapper {
    public static TrainingResponseDTO toTrainingResponseDTO(Training training) {
        TrainingResponseDTO trainingResponseDTO = new TrainingResponseDTO();
        trainingResponseDTO.setId(training.getId());
        trainingResponseDTO.setTrainee(TraineeMapper.toTraineeDTO(training.getTrainee()));
        trainingResponseDTO.setTrainer(TrainerMapper.toTrainerDTO(training.getTrainer()));
        trainingResponseDTO.setTrainingName(training.getTrainingName());
        trainingResponseDTO.setTrainingType(TrainingTypeMapper
                .toTrainingTypeDTO(training.getTrainingType()));
        trainingResponseDTO.setTrainingDate(training.getTrainingDate());
        trainingResponseDTO.setTrainingDuration(training.getTrainingDuration());
        return trainingResponseDTO;
    }
}
