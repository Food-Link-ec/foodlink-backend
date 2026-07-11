package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.EmailInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Email {

    private static final Pattern PATRON = Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");
    private static final int LONGITUD_MAXIMA = 100;

    private final String valor;

    private Email(String valor) {
        this.valor = valor;
    }

    public static Email de(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new EmailInvalidoException("El email es obligatorio");
        }
        if (valor.length() > LONGITUD_MAXIMA) {
            throw new EmailInvalidoException("El email no puede superar los " + LONGITUD_MAXIMA + " caracteres");
        }
        if (valor.contains(" ")) {
            throw new EmailInvalidoException("El email '" + valor + "' no puede contener espacios");
        }
        String limpio = valor.toLowerCase();
        if (!PATRON.matcher(limpio).matches()) {
            throw new EmailInvalidoException("El email '" + valor + "' no tiene un formato válido");
        }
        return new Email(limpio);
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email other)) {
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
