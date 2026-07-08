package com.foodlink.application.dto.response;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp
) {
}
