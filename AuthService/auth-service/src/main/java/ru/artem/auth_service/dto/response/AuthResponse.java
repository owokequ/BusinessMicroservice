package ru.artem.auth_service.dto.response;

public record AuthResponse(String tokenAccess, String tokenRefresh) {
}