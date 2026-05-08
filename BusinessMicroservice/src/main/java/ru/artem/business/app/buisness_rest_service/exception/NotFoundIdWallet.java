package ru.artem.business.app.buisness_rest_service.exception;

public class NotFoundIdWallet extends RuntimeException{

    public NotFoundIdWallet(String message) {
        super(message);
    }

}
