package ru.artem.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }

}