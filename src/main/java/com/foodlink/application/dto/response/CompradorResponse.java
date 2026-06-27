package com.foodlink.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompradorResponse(
        UUID id,
        String cedula,
        String nombre,
        String apellido,
        String email,
        boolean activo,
        LocalDateTime fechaRegistro
) {
}