package com.iasdietideals24.backend.exceptions;

public class IllegalDeleteRequestException extends InvalidParameterException {

    public IllegalDeleteRequestException() {
    }

    public IllegalDeleteRequestException(String message) {
        super(message);
    }
}
