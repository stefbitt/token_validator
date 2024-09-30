package com.itau.auth.token_validator.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable e) {
        super(message, e);
    }
}
