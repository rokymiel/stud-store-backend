package ru.hse.store.restApi.exceptions;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super("invalid request: " + message);
    }

}
