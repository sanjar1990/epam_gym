package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class Trainee extends User {
    // TODO
    //  Superclass already has an id field, so we don't need it in child classes
    // Done
    private LocalDate dateOfBirth;
    private String address;
}
