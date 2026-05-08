package ru.artem.business.app.buisness_rest_service.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, new ExceptionResponse(
            HttpStatus.BAD_REQUEST.value(),
            errors,
            LocalDateTime.now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, new ExceptionResponse(
            HttpStatus.NOT_FOUND.value(),
            List.of(ex.getMessage()),
            LocalDateTime.now()
        ));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleEmailExist(EmailAlreadyExistException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, new ExceptionResponse(
            HttpStatus.CONFLICT.value(),
            List.of(ex.getMessage()),
            LocalDateTime.now()
        ));
    }

    @ExceptionHandler(NotFoundIdWallet.class)
    public ResponseEntity<ExceptionResponse> handleWalletExist(NotFoundIdWallet ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, new ExceptionResponse(
            HttpStatus.NOT_FOUND.value(),
            List.of(ex.getMessage()),
            LocalDateTime.now()
        ));
    }

    @ExceptionHandler(NotEnoughMoney.class)
    public ResponseEntity<ExceptionResponse> handleMoneyExist(NotEnoughMoney ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, new ExceptionResponse(
            HttpStatus.CONFLICT.value(),
            List.of(ex.getMessage()),
            LocalDateTime.now()
        ));
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionResponse> handleOptimosticLock(ObjectOptimisticLockingFailureException ex) {
        log.error("Конфликт обновления");
        return buildErrorResponse(HttpStatus.CONFLICT, new ExceptionResponse(
            HttpStatus.CONFLICT.value(),
            List.of("Конфликт обновления, повторите операцию еще раз"),
            LocalDateTime.now()
        ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new ExceptionResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            List.of("Internal server"),
            LocalDateTime.now()
        ));
    }

     
    private ResponseEntity<ExceptionResponse> buildErrorResponse(HttpStatus status, ExceptionResponse errors) {
        return ResponseEntity.status(status).body(errors);
    }
}


