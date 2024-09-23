package com.iasdietideals24.backend.exceptions;

public class IdNotFoundException extends InvalidParameterException {

    public IdNotFoundException() {
    }

    public IdNotFoundException(String message) {
        super(message);
    }
}
