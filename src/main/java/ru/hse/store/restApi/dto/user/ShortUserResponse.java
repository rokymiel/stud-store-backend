package ru.hse.store.restApi.dto.user;

public record ShortUserResponse(
        String firstName,
        String lastName,
        int eduYear,
        String eduProgram
) {

}