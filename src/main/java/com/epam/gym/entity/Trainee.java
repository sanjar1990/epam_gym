package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
public class Trainee extends User {
    private String id;
    private LocalDate dateOfBirth;
    private String address;
}
