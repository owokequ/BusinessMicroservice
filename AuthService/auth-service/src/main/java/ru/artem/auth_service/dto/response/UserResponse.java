package ru.artem.auth_service.dto.response;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String email, Set<String> roles,
                String accessToken, String refreshToken) {
}