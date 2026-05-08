package ru.artem.business.app.buisness_rest_service.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(
    int status,
    List<String> message,
    LocalDateTime time
) {}
