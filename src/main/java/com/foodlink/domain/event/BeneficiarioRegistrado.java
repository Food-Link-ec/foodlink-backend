package com.foodlink.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record BeneficiarioRegistrado(
        UUID beneficiarioId,
        String ruc,
        String nombre,
        LocalDateTime ocurridoEn
) {
}