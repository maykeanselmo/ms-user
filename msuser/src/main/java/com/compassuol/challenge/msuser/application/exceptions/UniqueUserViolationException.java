package com.compassuol.challenge.msuser.application.exceptions;

public class UniqueUserViolationException extends RuntimeException {
    public UniqueUserViolationException(String message) {
        super(message);
    }
}
