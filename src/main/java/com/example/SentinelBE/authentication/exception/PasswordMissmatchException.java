package com.example.SentinelBE.authentication.exception;

public class PasswordMissmatchException extends RuntimeException {
    public PasswordMissmatchException() {
    }

    public PasswordMissmatchException(String message) {
        super(message);
    }

    public PasswordMissmatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
