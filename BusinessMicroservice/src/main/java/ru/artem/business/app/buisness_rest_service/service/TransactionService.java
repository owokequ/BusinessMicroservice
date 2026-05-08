package ru.artem.business.app.buisness_rest_service.service;

import java.util.List;

import ru.artem.business.app.buisness_rest_service.dto.request.TransactionUpdateDto;
import ru.artem.business.app.buisness_rest_service.dto.request.TransactionalCreateTransaferDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionTransferResponseDto;

public interface TransactionService {

    // TransactionResponseDto createTransaction(Long id, TransactionCreateDto dto);

    void deleteTransaction(String id);

    TransactionResponseDto getTransaction(String id);

    TransactionResponseDto updateTransactionDetails(String id, TransactionUpdateDto dto);

    List<TransactionResponseDto> getAllTransactions();

    TransactionTransferResponseDto moneyTransfer(TransactionalCreateTransaferDto dto);
}
