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
    // TODO:
    //  Is there a specific reason for using Double for duration?
    //  Seems long or int would be more appropriate.
    //  Also imagine a new team member is reading your code, how will they understand what the duration represents?
    //  Is it in minutes, hours, seconds, etc.? You can for example leave a comment or rename the variable.
    private Double duration;
}
