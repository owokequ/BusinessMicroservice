package ru.artem.business.app.buisness_rest_service.exception;

public class NotEnoughMoney extends RuntimeException{

    public NotEnoughMoney(String message) {
        super(message);
    }

}
