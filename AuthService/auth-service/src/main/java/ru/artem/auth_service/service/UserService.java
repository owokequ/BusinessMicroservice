package ru.artem.auth_service.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.artem.auth_service.config.util.JwtTokenUtil;
import ru.artem.auth_service.custom.CustomUserDetails;
import ru.artem.auth_service.dto.request.BusinessUserRequest;
import ru.artem.auth_service.dto.request.UserRequest;
import ru.artem.auth_service.dto.response.JwtClaims;
import ru.artem.auth_service.dto.response.UserResponse;
import ru.artem.auth_service.entity.RefreshToken;
import ru.artem.auth_service.entity.Role;
import ru.artem.auth_service.entity.User;
import ru.artem.auth_service.external.AuthHttpClient;
import ru.artem.auth_service.repository.RefreshTokenRepository;
import ru.artem.auth_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

        private final UserRepository userRepository;
        private final RoleService roleService;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenUtil jwtTokenUtil;
        private final RefreshTokenRepository refreshTokenRepository;
        private final AuthHttpClient authHttpClient;

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Пользователь не найден: " + username));

                return new CustomUserDetails(
                                user.getId(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                                                .toList());
        }

        public UserResponse registerUser(UserRequest user) {
                if (userRepository.existsByEmail(user.email())) {
                        throw new RuntimeException("Пользователь с таким именем уже существует");
                }
                Role role = roleService.checkUserRole();

                User newUser = new User();
                newUser.setEmail(user.email());
                newUser.setPassword(passwordEncoder.encode(user.password()));
                newUser.setRoles(Set.of(role));
                User savedUser = userRepository.save(newUser);

                JwtClaims resUser = new JwtClaims(
                                newUser.getEmail(),
                                newUser.getRoles().stream()
                                                .map(r -> r.getName()).collect(Collectors.toList()));

                String accessToken = jwtTokenUtil.generateAccessToken(resUser);
                String refreshToken = jwtTokenUtil.generateRefreshToken(resUser);
                RefreshToken tokenToSave = new RefreshToken();
                tokenToSave.setUser(newUser);
                tokenToSave.setToken(refreshToken);

                refreshTokenRepository.save(tokenToSave);

                BusinessUserRequest businessUserRequest = new BusinessUserRequest(
                                savedUser.getId(),
                                savedUser.getEmail());

                try {
                        authHttpClient.createUser(businessUserRequest);
                        log.info("User {} synced with BusinessService", savedUser.getEmail());
                } catch (Exception e) {
                        log.error("Failed to sync user with BusinessService: {}", e.getMessage());
                }

                return new UserResponse(
                                UUID.fromString(savedUser.getId()),
                                savedUser.getEmail(),
                                savedUser.getRoles().stream()
                                                .map(r -> r.getName())
                                                .collect(java.util.stream.Collectors.toSet()),
                                accessToken,
                                refreshToken);
        }

        public User getUserByUsername(String username) {
                return userRepository.findByEmail(username)
                                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        }
}