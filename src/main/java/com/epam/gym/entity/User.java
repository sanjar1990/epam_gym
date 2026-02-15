package com.epam.gym.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class User {
    // TODO:
    //  Using String as an identifier is not incorrect in general,
    //  but for this learning project it may complicate things further in the course.
    //  I’d recommend switching to Long or if you want to keep string-based identifiers,
    //  consider using UUID as a type instead of plain String.
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
