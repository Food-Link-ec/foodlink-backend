package com.foodlink.application.dto.response;

import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.Modalidad;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LoteResponse(
        UUID id,
        UUID comercioId,
        Modalidad modalidad,
        EstadoLote estado,
        double cantidadKg,
        BigDecimal precio,
        LocalDateTime fechaCaducidad,
        LocalDateTime fechaPublicacion,
        String descripcion,
        List<String> fotosUrl
) {
}
