package com.foodlink.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LoteResponse(
        UUID id,
        UUID comercioId,
        String modalidad,
        String estado,
        double cantidadKg,
        BigDecimal precioReducido,
        BigDecimal precioNormal,
        LocalDateTime fechaCaducidad,
        LocalDateTime fechaPublicacion,
        String descripcion,
        List<String> fotosUrl,
        Double latitud,
        Double longitud
) {
}