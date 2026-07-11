package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.TelefonoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class TelefonoEcuatoriano {

    private static final Pattern CELULAR = Pattern.compile("^09[6-9]\\d{7}$");
    private static final Pattern FIJO = Pattern.compile("^0[2-7]\\d{7}$");

    private final String valor;

    private TelefonoEcuatoriano(String valor) {
        this.valor = valor;
    }

    public static TelefonoEcuatoriano de(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new TelefonoInvalidoException("El teléfono es obligatorio");
        }
        String limpio = limpiar(valor);
        if (!CELULAR.matcher(limpio).matches() && !FIJO.matcher(limpio).matches()) {
            throw new TelefonoInvalidoException("El teléfono '" + valor + "' no es un número ecuatoriano válido");
        }
        return new TelefonoEcuatoriano(limpio);
    }

    private static String limpiar(String valor) {
        String limpio = valor.replaceAll("[\\s\\-()]", "");
        if (limpio.startsWith("+593")) {
            limpio = "0" + limpio.substring(4);
        } else if (limpio.startsWith("593")) {
            limpio = "0" + limpio.substring(3);
        }
        return limpio;
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TelefonoEcuatoriano other)) {
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
