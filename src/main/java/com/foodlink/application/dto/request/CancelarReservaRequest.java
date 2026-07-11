package com.foodlink.application.dto.request;

import java.util.UUID;

public record CancelarReservaRequest(
        UUID loteId,
        String motivo
) {
}