package com.foodlink.domain.event;

import com.foodlink.domain.model.lote.Modalidad;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoteRegistrado(UUID loteId, UUID comercioId, Modalidad modalidad, LocalDateTime ocurridoEn) {
}
