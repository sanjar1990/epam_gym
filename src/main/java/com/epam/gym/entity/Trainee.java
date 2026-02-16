package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Trainee extends User {
    private String id;
    private LocalDate dateOfBirth;
    private String address;
}
