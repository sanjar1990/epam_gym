package com.epam.gym.exceptions.handler;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private final int status;

    public APIException(String message) {
        super(message);
        this.status = 400;
    }

}