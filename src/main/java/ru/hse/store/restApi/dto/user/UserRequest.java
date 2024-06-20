package ru.hse.store.restApi.dto.user;

public record UserRequest(
    String email,
    String password,
    String firstName,
    String lastName,
    int eduYear,
    String eduProgram
) {

}
