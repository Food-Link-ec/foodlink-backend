package com.foodlink.application.dto.request;

import java.util.UUID;

public record ConfirmarDonacionRequest(
        UUID loteId,
        UUID organizacionId
) {
}
