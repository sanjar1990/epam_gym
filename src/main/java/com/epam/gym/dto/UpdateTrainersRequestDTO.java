package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTrainersRequestDTO {
    private String traineeUsername;
    private List<String> trainerUsernames;
}
