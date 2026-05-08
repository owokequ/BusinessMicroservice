package ru.artem.auth_service.dto.response;

import java.util.List;

public record JwtClaims(
        String username,
        List<String> roles) {

}
