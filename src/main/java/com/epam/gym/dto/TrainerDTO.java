package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerDTO {

    private Long id;
    private UserDTO user;
    private TrainingTypeDTO trainingType;
}
