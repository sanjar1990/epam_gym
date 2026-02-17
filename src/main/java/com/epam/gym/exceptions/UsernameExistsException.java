package com.epam.gym.exceptions;

public class UsernameExistsException extends RuntimeException{
    public UsernameExistsException(String message){
        super(message);
    }
}
