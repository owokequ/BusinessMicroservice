package ru.artem.business.app.buisness_rest_service.dto.request;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import ru.artem.business.app.buisness_rest_service.enums.CategoryEnum;

public record TransactionUpdateDto(

    @NotNull(message = "Надо выбрать категорию")
    CategoryEnum category,

    @Length(max = 100, message = "Ошибка! Не больше 100 символов на описание")
    String description

) {

}
