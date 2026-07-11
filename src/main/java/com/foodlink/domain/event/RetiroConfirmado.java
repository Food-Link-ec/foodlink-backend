package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetiroConfirmado(
        UUID loteId,
        UUID comercioId,
        UUID receptorId,
        double cantidadKg,
        String pin,
        LocalDateTime ocurridoEn
) {
}