package ru.artem.business.app.buisness_rest_service.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ru.artem.business.app.buisness_rest_service.enums.CategoryEnum;

public record TransactionResponseDto(
        String id,
        BigDecimal transactionAmount,
        CategoryEnum category,
        String description,
        LocalDateTime time,
        String walletId) {

}
