package com.epam.gym.entity;

import com.epam.gym.enums.TrainingType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Training {
    private Long id= ThreadLocalRandom.current().nextLong();
    private String trainerId;
    private String traineeId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    // TODO
    //  Is there a specific reason for using Double instead of Integer or Long for duration?
    //  Also imagine a new team member is reading your code, how will they understand what the duration represents?
    //  Is it in minutes, hours, seconds, etc.? You can for example leave a comment or give a field more descriptive name
    // Done
    private Integer trainingDuration;
}
