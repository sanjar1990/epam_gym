package com.epam.gym.mapper.training;

import com.epam.gym.dto.TraineeTrainingResponseDTO;
import com.epam.gym.dto.TrainerTrainingResponseDTO;
import com.epam.gym.entity.Training;
import com.epam.gym.mapper.trainee.TraineeMapper;
import com.epam.gym.mapper.trainer.TrainerMapper;
import com.epam.gym.mapper.training_type.TrainingTypeMapper;

public class TrainingMapper {
    public static TraineeTrainingResponseDTO toTraineeTrainingResponseDTO(Training training) {
        TraineeTrainingResponseDTO trainingResponseDTO = new TraineeTrainingResponseDTO();
        trainingResponseDTO.setId(training.getId());
        trainingResponseDTO.setTrainer(TrainerMapper.toTrainerDTO(training.getTrainer()));
        trainingResponseDTO.setTrainingType(TrainingTypeMapper
                .toTrainingTypeDTO(training.getTrainingType()));
        trainingResponseDTO.setTrainingDate(training.getTrainingDate());
        trainingResponseDTO.setTrainingDuration(training.getTrainingDuration());
        return trainingResponseDTO;
    }

    public static TrainerTrainingResponseDTO toTrainerTrainingResponseDTO(Training training) {
        TrainerTrainingResponseDTO trainingResponseDTO = new TrainerTrainingResponseDTO();
        trainingResponseDTO.setId(training.getId());
        trainingResponseDTO.setTrainer(TraineeMapper.toTraineeDTO(training.getTrainee()));
        trainingResponseDTO.setTrainingType(TrainingTypeMapper
                .toTrainingTypeDTO(training.getTrainingType()));
        trainingResponseDTO.setTrainingDate(training.getTrainingDate());
        trainingResponseDTO.setTrainingDuration(training.getTrainingDuration());
        return trainingResponseDTO;
    }
}
