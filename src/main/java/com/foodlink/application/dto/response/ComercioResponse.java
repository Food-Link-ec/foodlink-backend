package com.foodlink.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ComercioResponse(
        UUID id,
        String ruc,
        String nombre,
        String telefono,
        String email,
        String estado,
        LocalDateTime fechaRegistro
) {
}