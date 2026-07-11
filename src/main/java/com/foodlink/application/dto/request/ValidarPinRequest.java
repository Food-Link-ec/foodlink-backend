package com.foodlink.application.dto.request;

import java.util.UUID;

public record ValidarPinRequest(
        UUID loteId,
        String pin
) {
}