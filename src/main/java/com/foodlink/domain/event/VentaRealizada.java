package com.foodlink.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VentaRealizada(UUID loteId, UUID compradorId, BigDecimal montoPageado, LocalDateTime ocurridoEn) {
}
