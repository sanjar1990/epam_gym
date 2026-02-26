package com.epam.gym.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTraineeCreateRequestDTO {
    @NotBlank(message = "First name cannot be empty or null")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty or null")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
