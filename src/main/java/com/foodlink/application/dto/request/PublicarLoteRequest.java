package com.foodlink.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PublicarLoteRequest(
        @NotBlank String modalidad,
        double cantidadKg,
        BigDecimal precio,
        BigDecimal precioMercado,
        @NotNull LocalDateTime fechaCaducidad,
        @NotBlank String descripcion,
        @NotEmpty List<String> fotosUrl,
        Double latitud,
        Double longitud,
        String categoriaProducto
) {
}
