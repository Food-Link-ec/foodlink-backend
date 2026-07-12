package com.foodlink.application.dto.request;

import java.util.UUID;

public record BuscarLotesRequest(
        String modalidad,
        String estado,
        UUID comercioId,
        Double latitud,
        Double longitud,
        Double radioKm
) {
}
