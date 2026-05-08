package ru.artem.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.dto.request.AuthRequest;
import ru.artem.auth_service.dto.request.RefreshTokenRequest;
import ru.artem.auth_service.dto.response.AuthAccessResponse;
import ru.artem.auth_service.dto.response.AuthResponse;
import ru.artem.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        AuthResponse tokens = authService.authenticate(authRequest.email(), authRequest.password());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessResponse> refreshToken(@RequestBody RefreshTokenRequest token) {
        AuthAccessResponse accessToken = authService.refreshAccessToken(token);
        return ResponseEntity.ok(accessToken);
    }

    @DeleteMapping("/{id}/logout")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        authService.logout(id);
        return ResponseEntity.noContent().build();
    }
}