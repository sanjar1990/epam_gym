package com.epam.gym.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    // TODO:
    //  Add validations for username and password
    @NotBlank(message = "Username cannot be empty or null")
    private String username;
    @NotBlank(message = "Password cannot be empty or null")
    private String password;
}
