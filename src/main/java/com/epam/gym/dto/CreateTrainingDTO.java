package com.epam.gym.dto;

import com.epam.gym.entity.Trainee;
import com.epam.gym.entity.Trainer;
import com.epam.gym.entity.TrainingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTrainingDTO {
    private Long trainerId;
    private String trainingName;
    private Long trainingTypeId;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}
