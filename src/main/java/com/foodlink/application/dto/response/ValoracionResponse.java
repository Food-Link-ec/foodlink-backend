package com.foodlink.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ValoracionResponse(
        Long id,
        UUID loteId,
        UUID comercioId,
        int puntuacion,
        String comentario,
        LocalDateTime creadoEn
) {
}
