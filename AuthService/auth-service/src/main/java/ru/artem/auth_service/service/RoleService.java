package ru.artem.auth_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.artem.auth_service.entity.Role;
import ru.artem.auth_service.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // public org.springframework.security.core.GrantedAuthority getRoleByName(String roleName) {
    //     return () -> roleName;
    // }

    public Role checkUserRole() {

        return roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Такой роли нет"));

    }
}
