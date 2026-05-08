package ru.artem.business.app.buisness_rest_service.service;

import ru.artem.business.app.buisness_rest_service.dto.request.WalletCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.WalletResponseDto;

public interface WalletService {

    WalletResponseDto addMoney(String id, WalletCreateDto dto);

    WalletResponseDto subtractMoney(String id, WalletCreateDto dto);

    WalletResponseDto getBalance(String id);
}
