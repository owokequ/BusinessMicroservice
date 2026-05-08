package ru.artem.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.artem.auth_service.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(String username);
}
