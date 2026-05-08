package ru.artem.business.app.buisness_rest_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.artem.business.app.buisness_rest_service.dto.request.TransactionUpdateDto;
import ru.artem.business.app.buisness_rest_service.dto.request.TransactionalCreateTransaferDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionTransferResponseDto;
import ru.artem.business.app.buisness_rest_service.service.TransactionService;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // @PostMapping("/{id}")
    // public ResponseEntity<TransactionResponseDto> createTransaction(@PathVariable
    // Long id, @Valid @RequestBody TransactionCreateDto dto) {
    // TransactionResponseDto transaction = transactionService.createTransaction(id,
    // dto);
    // return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponseDto>> getAllTransaction() {
        List<TransactionResponseDto> transaction = transactionService.getAllTransactions();
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransaction(@PathVariable String id) {
        TransactionResponseDto transaction = transactionService.getTransaction(id);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable String id,
            @Valid @RequestBody TransactionUpdateDto dto) {
        TransactionResponseDto transaction = transactionService.updateTransactionDetails(id, dto);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionTransferResponseDto> moneyTransfer(
            @RequestBody TransactionalCreateTransaferDto dto) {
        TransactionTransferResponseDto transfer = transactionService.moneyTransfer(dto);

        return ResponseEntity.ok().body(transfer);
    }
}
