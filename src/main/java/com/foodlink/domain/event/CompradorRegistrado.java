package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompradorRegistrado(
        UUID compradorId,
        String cedula,
        String nombre,
        LocalDateTime ocurridoEn
) {
}