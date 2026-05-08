package ru.artem.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.artem.auth_service.entity.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);

    Optional<Role> findByName(String name);
}   
