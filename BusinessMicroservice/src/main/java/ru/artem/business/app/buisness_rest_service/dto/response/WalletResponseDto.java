package ru.artem.business.app.buisness_rest_service.dto.response;

import java.math.BigDecimal;

public record WalletResponseDto(
    String id,
    BigDecimal amount
) {
    
}
