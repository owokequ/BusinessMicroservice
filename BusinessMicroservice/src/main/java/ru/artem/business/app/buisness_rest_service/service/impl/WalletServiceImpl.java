package ru.artem.business.app.buisness_rest_service.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.artem.business.app.buisness_rest_service.dto.request.WalletCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.WalletResponseDto;
import ru.artem.business.app.buisness_rest_service.entity.Wallet;
import ru.artem.business.app.buisness_rest_service.exception.NotEnoughMoney;
import ru.artem.business.app.buisness_rest_service.exception.NotFoundIdWallet;
import ru.artem.business.app.buisness_rest_service.repository.WalletRepository;
import ru.artem.business.app.buisness_rest_service.service.WalletService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public WalletResponseDto addMoney(String id, WalletCreateDto dto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> {
            log.error("Кошелек с id: " + id + " не найден");
            return new NotFoundIdWallet("Нет кошелька с таким id: " + id);
        });

        BigDecimal newMoney = wallet.getAmount().add(dto.amount());

        wallet.setAmount(newMoney);

        Wallet newWallet = walletRepository.save(wallet);
        return new WalletResponseDto(
                newWallet.getId(),
                newWallet.getAmount());
    }

    @Override
    public WalletResponseDto getBalance(String id) {
        Wallet get = walletRepository.findById(id).orElseThrow(() -> {
            log.error("Кошелек с id: " + id + " не найден");
            return new NotFoundIdWallet("Нет кошелька с таким id: " + id);
        });

        return new WalletResponseDto(
                get.getId(),
                get.getAmount());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public WalletResponseDto subtractMoney(String id, WalletCreateDto dto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> {
            log.error("Кошелек с id: " + id + " не найден");
            return new NotFoundIdWallet("Нет кошелька с таким id: " + id);
        });

        BigDecimal newAmount = wallet.getAmount().subtract(dto.amount());

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            log.error("У пользователя с id: " + id + " недостаточно средств");
            throw new NotEnoughMoney("Недостаточно средств");
        }
        // try {
        // Thread.sleep(5000);
        // } catch (Exception e) {
        // log.error("лялял тополя, " + e);
        // }

        wallet.setAmount(newAmount);

        Wallet saved = walletRepository.save(wallet);

        return new WalletResponseDto(saved.getId(), saved.getAmount());
    }

}
