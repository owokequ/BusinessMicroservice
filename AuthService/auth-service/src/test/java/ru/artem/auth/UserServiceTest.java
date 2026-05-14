package ru.artem.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.artem.auth_service.config.util.JwtTokenUtil;
import ru.artem.auth_service.dto.request.BusinessUserRequest;
import ru.artem.auth_service.dto.request.UserRequest;
import ru.artem.auth_service.dto.response.UserResponse;
import ru.artem.auth_service.entity.RefreshToken;
import ru.artem.auth_service.entity.Role;
import ru.artem.auth_service.entity.User;
import ru.artem.auth_service.external.AuthHttpClient;
import ru.artem.auth_service.repository.RefreshTokenRepository;
import ru.artem.auth_service.repository.UserRepository;
import ru.artem.auth_service.service.RoleService;
import ru.artem.auth_service.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @Mock
    private AuthHttpClient authHttpClient;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByUsername_ShouldReturnUser(){
        String username = "artem";

        User user = new User(
            "artem@gmail.com",
            "123",
            Set.of(new Role("USER"))
        );

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(username);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getRoles()).isEqualTo(user.getRoles());

    }

    @Test
    void registerUser_SuccessShouidReturnUserResponse(){
        UserRequest userReq = new UserRequest(
            "artem@gmail.com",
            "123");
        Role defRole = new Role("USER");
        User savedUser = new User();
        savedUser.setId(UUID.randomUUID().toString());
        savedUser.setEmail(userReq.email());
        savedUser.setPassword("hashPass");
        savedUser.setRoles(Set.of(defRole));

        when(userRepository.existsByEmail(userReq.email())).thenReturn(false);
        when(roleService.checkUserRole()).thenReturn(defRole);
        when(passwordEncoder.encode(userReq.password())).thenReturn("hashPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn("access-token");
        when(jwtTokenUtil.generateRefreshToken(any())).thenReturn("refresh-token");

        UserResponse result = userService.registerUser(userReq);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(userReq.email());
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
        assertThat(result.roles()).contains("USER");

        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(authHttpClient).createUser(any(BusinessUserRequest.class));
    }
}
