package com.epam.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTraineeRequestDTO {
    @NotBlank(message = "First name cannot be empty or null")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty or null")
    private String lastName;
    // TODO:
    //  Can I call the API with a date of birth in the future?
    //  Also useful to explicitly specify format, let's keep in mind that a client can send all sorts of garbage data
    //  Our responsibility is to be able to answer for each case with a proper error message
    private LocalDate dateOfBirth;
    private String address;
}
