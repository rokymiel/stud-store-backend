package ru.hse.store.restApi.dto.auth;

public record RefreshTokenResponse(String accessToken, String refreshToken) {

}