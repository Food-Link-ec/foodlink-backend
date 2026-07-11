package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoteReservado(UUID loteId, UUID beneficiarioId, LocalDateTime inicioReserva, LocalDateTime ocurridoEn) {
}
