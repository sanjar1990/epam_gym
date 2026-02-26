package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrainingResponseDTO {
    private Long id;
    private TraineeDTO trainee;
    private TrainerDTO trainer;
    private String trainingName;
    private TrainingTypeDTO trainingType;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}
