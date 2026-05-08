package ru.artem.business.app.buisness_rest_service.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateDto(
        @Id String id,

        @Email(message = "Не корректный email") String email) {
}
