package com.iasdietideals24.backend.exceptions;

public class AuthRuntimeException extends RuntimeException {

    public AuthRuntimeException() {
    }

    public AuthRuntimeException(String message) {
        super(message);
    }
}
