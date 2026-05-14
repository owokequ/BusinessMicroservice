package ru.artem.auth_service.controller;

import java.util.Arrays;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.dto.request.AuthRequest;
import ru.artem.auth_service.dto.request.RefreshTokenRequest;
import ru.artem.auth_service.dto.request.UserRequest;
import ru.artem.auth_service.dto.response.AuthAccessResponse;
import ru.artem.auth_service.dto.response.AuthResponse;
import ru.artem.auth_service.dto.response.UserResponse;
import ru.artem.auth_service.service.AuthService;
import ru.artem.auth_service.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        
        AuthResponse tokens = authService.authenticate(authRequest.email(), authRequest.password());
        
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.tokenRefresh())
        .httpOnly(false)
        .secure(false)
        .maxAge(30*24*60*60)
        .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest, HttpServletResponse response) {
        UserResponse newUser = userService.registerUser(userRequest);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newUser.refreshToken())
        .httpOnly(false)
        .secure(false)
        .maxAge(30*24*60*60)
        .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessResponse> refreshToken(HttpServletRequest request) {

        Cookie[] massCookies = request.getCookies();
        if(massCookies == null){
            throw new RuntimeException("cookie not found");
        }

        String refresh = Arrays.stream(massCookies)
                        .filter(c -> "refresh_token".equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Refresh_token not found in cookie"));

        AuthAccessResponse accessToken = authService.refreshAccessToken(
            new RefreshTokenRequest(refresh)
        );
        return ResponseEntity.ok(accessToken);
    }

    @DeleteMapping("/{id}/logout")
    public ResponseEntity<Void> deleteUser(@PathVariable String id, HttpServletResponse response) {

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
        .httpOnly(false)
        .secure(false)
        .maxAge(0)
        .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
        authService.logout(id);
        return ResponseEntity.noContent().build();
    }
}