package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetTraineeTrainingsCriteriaFilterDTO {
    private String traineeUsername;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String trainerName;
    private String trainingType;
}
