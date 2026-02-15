package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
public class Trainee extends User {
    // TODO:
    //  Superclass already has an id field, so we should remove the id field from child classes
    private String id;
    private LocalDate dateOfBirth;
    private String address;
}
