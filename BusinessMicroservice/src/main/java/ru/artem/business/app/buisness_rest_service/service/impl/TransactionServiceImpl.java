package ru.artem.business.app.buisness_rest_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.artem.business.app.buisness_rest_service.dto.request.TransactionUpdateDto;
import ru.artem.business.app.buisness_rest_service.dto.request.TransactionalCreateTransaferDto;
import ru.artem.business.app.buisness_rest_service.dto.request.WalletCreateDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.TransactionTransferResponseDto;
import ru.artem.business.app.buisness_rest_service.dto.response.WalletResponseDto;
import ru.artem.business.app.buisness_rest_service.entity.Transaction;
import ru.artem.business.app.buisness_rest_service.entity.Wallet;
import ru.artem.business.app.buisness_rest_service.enums.TransferStatus;
import ru.artem.business.app.buisness_rest_service.exception.ResourceNotFoundException;
import ru.artem.business.app.buisness_rest_service.repository.TransactionRepository;
import ru.artem.business.app.buisness_rest_service.repository.WalletRepository;
import ru.artem.business.app.buisness_rest_service.service.TransactionService;
import ru.artem.business.app.buisness_rest_service.service.WalletService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final WalletRepository walletRepository;

    // @Transactional
    // @Override
    // public TransactionResponseDto createTransaction(Long id, TransactionCreateDto
    // dto) {
    // Wallet wallet = walletRepository.findById(id).orElseThrow(() -> {
    // log.error("Кошелек с id: " + id + " не найден");
    // return new NotFoundIdWallet("Нет кошелька с таким id: " + id);
    // });

    // if(dto.transactionType() == TransactionType.ADD){
    // walletService.addMoney(id, new WalletCreateDto(dto.transaction_amount()));
    // } else{
    // walletService.subtractMoney(id, new
    // WalletCreateDto(dto.transaction_amount()));
    // }

    // Transaction transaction = transactionRepository.save(
    // new Transaction(dto.transaction_amount(), dto.category(), dto.description(),
    // wallet)
    // );

    // return new TransactionResponseDto(
    // transaction.getId(),
    // transaction.getTransactionAmount(),
    // transaction.getCategories(),
    // transaction.getDescription(),
    // LocalDateTime.now(),
    // wallet.getId()
    // );
    // }

    @Override
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);

    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(
                el -> new TransactionResponseDto(
                        el.getId(),
                        el.getTransactionAmount(),
                        el.getCategories(),
                        el.getDescription(),
                        el.getTransactionTime(),
                        el.getWallet().getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponseDto getTransaction(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id с такой транзакцией не существует"));
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionAmount(),
                transaction.getCategories(),
                transaction.getDescription(),
                transaction.getTransactionTime(),
                transaction.getWallet().getId());
    }

    @Transactional
    @Override
    public TransactionResponseDto updateTransactionDetails(String id, TransactionUpdateDto dto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id с такой транзакцией не существует"));
        transaction.setCategories(dto.category());
        transaction.setDescription(dto.description());
        transactionRepository.save(transaction);

        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionAmount(),
                transaction.getCategories(),
                transaction.getDescription(),
                transaction.getTransactionTime(),
                transaction.getWallet().getId());
    }

    @Transactional
    @Override
    public TransactionTransferResponseDto moneyTransfer(TransactionalCreateTransaferDto dto) {
        if (dto.userConsumer() == dto.userProducer()) {
            throw new ResourceNotFoundException("Вы отправляете самому себе");
        }
        Wallet walletProducer = walletRepository.findById(dto.userProducer())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Отправителя с таким id= " + dto.userConsumer() + " не существует"));

        Wallet walletConsumer = walletRepository.findById(dto.userConsumer()).orElseThrow(
                () -> new ResourceNotFoundException("Получателя с таким id= " + dto.userConsumer() + " не существует"));
        WalletResponseDto producer = walletService.subtractMoney(
                dto.userProducer(),
                new WalletCreateDto(dto.amount()));

        transactionRepository.save(new Transaction(
                dto.amount(),
                dto.category(),
                dto.description(),
                walletProducer));

        walletService.addMoney(dto.userConsumer(), new WalletCreateDto(dto.amount()));

        transactionRepository.save(new Transaction(
                dto.amount(),
                dto.category(),
                dto.description(),
                walletConsumer));

        return new TransactionTransferResponseDto(producer.id(), producer.amount(), TransferStatus.SUCCES);
    }

}
