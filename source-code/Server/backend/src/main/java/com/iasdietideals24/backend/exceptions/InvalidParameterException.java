package com.iasdietideals24.backend.exceptions;

public class InvalidParameterException extends Exception {

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }
}
