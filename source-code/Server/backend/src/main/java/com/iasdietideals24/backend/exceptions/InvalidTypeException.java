package com.iasdietideals24.backend.exceptions;

public class InvalidTypeException extends InvalidParameterException {

    public InvalidTypeException() {
    }

    public InvalidTypeException(String message) {
        super(message);
    }
}
