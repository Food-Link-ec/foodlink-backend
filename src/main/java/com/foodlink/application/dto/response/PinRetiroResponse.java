package com.foodlink.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PinRetiroResponse(
        UUID loteId,
        String pin,
        String qrData,
        LocalDateTime expiraEn,
        String mensaje
) {
}