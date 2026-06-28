package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComercioRegistrado(
        UUID comercioId,
        String ruc,
        String nombre,
        LocalDateTime ocurridoEn
) {
}