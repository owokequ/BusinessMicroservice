package ru.artem.auth_service.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.dto.request.UserRequest;
import ru.artem.auth_service.dto.response.UserResponse;
import ru.artem.auth_service.external.AuthHttpClient;
import ru.artem.auth_service.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class BaseController {

    private final UserService userService;

    private final AuthHttpClient authHttpClient;

    @GetMapping("/admin")
    public String home() {
        return "Welcome to ADMIN";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
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

}