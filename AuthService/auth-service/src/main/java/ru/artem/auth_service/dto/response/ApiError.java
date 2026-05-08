package ru.artem.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ApiError(
    int status,
    String message,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp
) {
    public ApiError(int status, String message) {
        this(status, message, LocalDateTime.now());
    }
}