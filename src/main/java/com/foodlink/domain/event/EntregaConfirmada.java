package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaConfirmada(UUID loteId, UUID comercioId, UUID receptorId, double cantidadKg, LocalDateTime ocurridoEn) {
}
