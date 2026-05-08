package ru.artem.business.app.buisness_rest_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.artem.business.app.buisness_rest_service.dto.request.UserCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserAndTransactionsResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserResponseDto;
import ru.artem.business.app.buisness_rest_service.entity.User;
import ru.artem.business.app.buisness_rest_service.exception.EmailAlreadyExistException;
import ru.artem.business.app.buisness_rest_service.exception.ResourceNotFoundException;
import ru.artem.business.app.buisness_rest_service.repository.UserRepository;
import ru.artem.business.app.buisness_rest_service.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserAndTransactionsResponseDto getUserAndTransactions(String id) {
        User user = userRepository.findAllTransactionsFromUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого пользователя нет"));

        return new UserAndTransactionsResponseDto(
                user.getId(),
                user.getEmail(),
                user.getWallet().getAmount(),
                user.getWallet().getTransactions().stream()
                        .map(el -> new TransactionResponseDto(
                                el.getId(),
                                el.getTransactionAmount(),
                                el.getCategories(),
                                el.getDescription(),
                                el.getTransactionTime(),
                                el.getWallet().getId()))
                        .toList());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public UserResponseDto createUser(UserCreateDto dto) {
        boolean userExist = userRepository.existsByEmail(dto.email());
        if (userExist) {
            throw new EmailAlreadyExistException("Такой пользователь уже существует");
        }
        User user = new User(dto.id(), dto.email());
        User newUser = userRepository.save(user);
        return new UserResponseDto(
                newUser.getId(),
                newUser.getEmail(),
                newUser.getWallet().getAmount());
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userRepository.findAll().stream().map(el -> new UserResponseDto(
                el.getId(),
                el.getEmail(),
                el.getWallet().getAmount())).toList();
    }

    @Override
    public UserResponseDto getUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getWallet().getAmount());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public UserResponseDto updateUser(String id, UserCreateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        user.setEmail(dto.email());

        User updateUser = userRepository.save(user);

        return new UserResponseDto(
                updateUser.getId(),
                updateUser.getEmail(),
                updateUser.getWallet().getAmount());
    }

}
