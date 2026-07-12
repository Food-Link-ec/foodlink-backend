package com.foodlink.application.dto.response;

import java.util.UUID;

public record ComercioTopResponse(
        UUID comercioId,
        String nombreComercio,
        long lotesEntregados,
        double kgRescatados
) {
}
