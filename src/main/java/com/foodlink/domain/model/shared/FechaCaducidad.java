package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.lote.exception.FechaCaducidadInvalidaException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class FechaCaducidad {

    private final LocalDateTime valor;

    private FechaCaducidad(LocalDateTime valor) {
        this.valor = valor;
    }

    public static FechaCaducidad de(LocalDateTime valor) {
        if (valor == null || valor.isBefore(LocalDateTime.now())) {
            throw new FechaCaducidadInvalidaException("La fecha de caducidad no puede estar en el pasado");
        }
        return new FechaCaducidad(valor);
    }

    public static FechaCaducidad reconstituir(LocalDateTime valor) {
        return new FechaCaducidad(valor);
    }

    public boolean haExpirado() {
        return LocalDateTime.now().isAfter(valor);
    }

    public long horasRestantes() {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), valor);
    }

    public LocalDateTime getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FechaCaducidad other)) {
            return false;
        }
        return valor.equals(other.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
