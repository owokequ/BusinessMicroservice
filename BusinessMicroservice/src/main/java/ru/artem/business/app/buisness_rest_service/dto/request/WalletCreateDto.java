package ru.artem.business.app.buisness_rest_service.dto.request;

import java.math.BigDecimal;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record WalletCreateDto(

    @NotNull(message = "Сумма не может быть null")
    @DecimalMin(value = "0.01", message = "Сумма не может быть меньше 0.01")
    @DecimalMax(value = "1000000000", message = "сумма не может превышать 1_000_000_000")
    BigDecimal amount

) {}
