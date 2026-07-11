package com.foodlink.domain.event;

import com.foodlink.domain.model.lote.Modalidad;

import java.time.LocalDateTime;
import java.util.UUID;

public record LotePublicado(UUID loteId, UUID comercioId, Modalidad modalidad, double cantidadKg, LocalDateTime ocurridoEn) {
}
