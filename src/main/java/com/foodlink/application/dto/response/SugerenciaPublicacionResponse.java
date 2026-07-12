package com.foodlink.application.dto.response;

public record SugerenciaPublicacionResponse(
        String fechaCaducidadSugerida,
        String categoriaProductoSugerida,
        String descripcionSugerida,
        String confianzaFecha,
        String fuenteOcr,
        String instruccion
) {
}
