package ru.artem.auth_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.config.util.JwtTokenUtil;
import ru.artem.auth_service.dto.request.RefreshTokenRequest;
import ru.artem.auth_service.dto.response.AuthAccessResponse;
import ru.artem.auth_service.dto.response.AuthResponse;
import ru.artem.auth_service.dto.response.JwtClaims;
import ru.artem.auth_service.entity.RefreshToken;
import ru.artem.auth_service.entity.User;
import ru.artem.auth_service.exception.InvalidCredentialsException;
import ru.artem.auth_service.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Неверный логин или пароль");
        }

        User user = userService.getUserByUsername(username);

        JwtClaims resUser = new JwtClaims(
                user.getEmail(),
                user.getRoles().stream()
                        .map(r -> r.getName()).collect(Collectors.toList()));

        String accessToken = jwtTokenUtil.generateAccessToken(resUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(resUser);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken tokenToSave;
        if (existingToken.isPresent()) {
            tokenToSave = existingToken.get();
            tokenToSave.setToken(refreshToken);
        } else {
            tokenToSave = new RefreshToken();
            tokenToSave.setUser(user);
            tokenToSave.setToken(refreshToken);
        }

        refreshTokenRepository.save(tokenToSave);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthAccessResponse refreshAccessToken(RefreshTokenRequest refreshToken) {
        if (refreshToken == null || refreshToken.refreshToken().isEmpty()) {
            throw new InvalidCredentialsException("Refresh token отсутствует");
        }

        refreshTokenRepository.findByToken(refreshToken.refreshToken())
                .orElseThrow(() -> new InvalidCredentialsException("Невалидный refresh token"));

        Claims data = jwtTokenUtil.validationRefreshToken(refreshToken.refreshToken());

        return new AuthAccessResponse(jwtTokenUtil.generateAccessToken(new JwtClaims(
                data.getSubject(),
                data.get("roles", List.class))));
    }

    public void logout(String id) {
        refreshTokenRepository.deleteTokenById(id);
    }
}