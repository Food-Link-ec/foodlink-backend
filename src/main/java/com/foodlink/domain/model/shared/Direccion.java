package com.foodlink.domain.model.shared;

public record Direccion(
        String calle,
        String ciudad,
        String provincia,
        String referencia,
        Double latitud,
        Double longitud
) {

    public Direccion {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad es obligatoria");
        }
        if (provincia == null || provincia.isBlank()) {
            throw new IllegalArgumentException("La provincia es obligatoria");
        }
    }
}