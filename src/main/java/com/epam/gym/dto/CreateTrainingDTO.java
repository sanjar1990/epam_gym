package com.epam.gym.dto;

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
