package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class User {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
