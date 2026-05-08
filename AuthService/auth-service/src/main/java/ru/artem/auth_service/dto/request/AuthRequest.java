package ru.artem.auth_service.dto.request;

public record AuthRequest(String email, String password) {
}