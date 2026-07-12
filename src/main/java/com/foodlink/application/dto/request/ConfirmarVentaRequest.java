package com.foodlink.application.dto.request;

import java.util.UUID;

public record ConfirmarVentaRequest(
        UUID loteId,
        UUID compradorId
) {
}
