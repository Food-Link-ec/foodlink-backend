package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoteExpirado(UUID loteId, UUID comercioId, LocalDateTime fechaCaducidad, LocalDateTime ocurridoEn) {
}
