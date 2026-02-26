package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TraineeDTO {
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private UserDTO user;
}
