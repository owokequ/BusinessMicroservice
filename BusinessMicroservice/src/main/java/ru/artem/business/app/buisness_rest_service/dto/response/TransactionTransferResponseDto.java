package ru.artem.business.app.buisness_rest_service.dto.response;

import java.math.BigDecimal;

import ru.artem.business.app.buisness_rest_service.enums.TransferStatus;

public record TransactionTransferResponseDto(
        String id,
        BigDecimal totalAmount,
        TransferStatus status) {

}
