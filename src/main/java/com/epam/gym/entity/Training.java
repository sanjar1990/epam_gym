package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
public class Training {
    private String id;
    private String trainerId;
    private String traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Double duration;
}
