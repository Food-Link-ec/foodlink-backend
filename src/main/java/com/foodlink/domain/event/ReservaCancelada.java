package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaCancelada(UUID loteId, UUID beneficiarioId, String motivo, LocalDateTime ocurridoEn) {
}
