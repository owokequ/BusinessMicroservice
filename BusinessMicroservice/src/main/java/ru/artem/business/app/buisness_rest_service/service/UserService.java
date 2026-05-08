package ru.artem.business.app.buisness_rest_service.service;

import java.util.List;

import ru.artem.business.app.buisness_rest_service.dto.request.UserCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserAndTransactionsResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.UserResponseDto;

public interface UserService {

    public UserResponseDto createUser(UserCreateDto dto);

    public UserResponseDto getUser(String id);

    public List<UserResponseDto> getAllUser();

    public void deleteUser(String id);

    public UserResponseDto updateUser(String id, UserCreateDto dto);

    public UserAndTransactionsResponseDto getUserAndTransactions(String id);
}
