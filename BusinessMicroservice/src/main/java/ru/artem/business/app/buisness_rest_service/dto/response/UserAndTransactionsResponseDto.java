package ru.artem.business.app.buisness_rest_service.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record UserAndTransactionsResponseDto(
                String id,
                String email,
                BigDecimal walletBalance,
                List<TransactionResponseDto> transactions) {

}
