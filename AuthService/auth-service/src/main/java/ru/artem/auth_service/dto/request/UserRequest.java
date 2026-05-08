package ru.artem.auth_service.dto.request;

public record UserRequest(String email, String password) {
}