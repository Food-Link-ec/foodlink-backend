package com.foodlink.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record BeneficiarioResponse(
        UUID id,
        String nombre,
        String ruc,
        String email,
        String estadoVerificacion,
        LocalDateTime fechaRegistro
) {
}