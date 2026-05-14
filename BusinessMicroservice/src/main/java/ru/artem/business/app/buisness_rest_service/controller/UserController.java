package ru.artem.business.app.buisness_rest_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.artem.business.app.buisness_rest_service.dto.request.UserCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserAndTransactionsResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserResponseDto;
import ru.artem.business.app.buisness_rest_service.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto dto) {
        UserResponseDto user = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<UserResponseDto> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String id) {
        UserResponseDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserCreateDto dto) {
        UserResponseDto user = userService.updateUser(id, dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/transactions")
    @PreAuthorize("#id == authentication.principal")
    public ResponseEntity<UserAndTransactionsResponseDto> getAllUserTransactions(@PathVariable String id) {
        UserAndTransactionsResponseDto users = userService.getUserAndTransactions(id);
        return ResponseEntity.ok(users);
    }

}