package ru.hse.store.restApi.dto.user;

public record UserResponse(
    String email,
    String firstName,
    String lastName,
    int eduYear,
    String eduProgram
) {

}
