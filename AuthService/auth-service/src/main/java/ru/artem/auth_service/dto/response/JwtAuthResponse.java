package ru.artem.auth_service.dto.response;

public record JwtAuthResponse(String accessToken, String refreshToken) {}