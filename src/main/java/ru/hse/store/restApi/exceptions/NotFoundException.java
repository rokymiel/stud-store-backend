package ru.hse.store.restApi.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz) {
        super(clazz.getSimpleName() + " not found");
    }
}
