package ru.artem.business.app.buisness_rest_service.dto.request;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import ru.artem.business.app.buisness_rest_service.enums.CategoryEnum;

public record TransactionalCreateTransaferDto(
        @NotNull(message = "Id не может быть null") String userProducer,

        @NotNull(message = "Id не может быть null") String userConsumer,

        @NotNull(message = "Транзакция не может быть null") BigDecimal amount,

        @NotNull(message = "Надо выбрать категорию") CategoryEnum category,

        @Length(max = 100, message = "Ошибка! Не больше 100 символов на описание") String description

) {

}
