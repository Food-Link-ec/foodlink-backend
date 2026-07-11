package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.NombreInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class NombreOrganizacion {

    private static final Pattern PATRON = Pattern.compile("^[a-zA-Z0-9áéíóúüñÁÉÍÓÚÜÑ.,&()\\- ]+$");
    private static final int LONGITUD_MINIMA = 2;
    private static final int LONGITUD_MAXIMA = 200;

    private final String valor;

    private NombreOrganizacion(String valor) {
        this.valor = valor;
    }

    public static NombreOrganizacion de(String valor) {
        if (valor == null) {
            throw new NombreInvalidoException("El nombre de la organización es obligatorio");
        }
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            throw new NombreInvalidoException("El nombre de la organización es obligatorio");
        }
        if (limpio.length() < LONGITUD_MINIMA || limpio.length() > LONGITUD_MAXIMA) {
            throw new NombreInvalidoException(
                    "El nombre de la organización debe tener entre " + LONGITUD_MINIMA + " y " + LONGITUD_MAXIMA + " caracteres");
        }
        if (!PATRON.matcher(limpio).matches()) {
            throw new NombreInvalidoException("El nombre '" + valor + "' contiene caracteres no permitidos");
        }
        return new NombreOrganizacion(limpio);
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NombreOrganizacion other)) {
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
        return valor;
    }
}
