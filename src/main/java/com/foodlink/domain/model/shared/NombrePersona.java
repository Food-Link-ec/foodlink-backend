package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.NombreInvalidoException;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public final class NombrePersona {

    private static final Pattern PATRON = Pattern.compile(
            "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ]+(?:[ -][a-zA-ZáéíóúüñÁÉÍÓÚÜÑ]+)*$");
    private static final Set<String> PREPOSICIONES = Set.of("de", "del", "la", "las", "los");
    private static final int LONGITUD_MINIMA = 2;
    private static final int LONGITUD_MAXIMA = 100;

    private final String valor;

    private NombrePersona(String valor) {
        this.valor = valor;
    }

    public static NombrePersona de(String valor) {
        if (valor == null) {
            throw new NombreInvalidoException("El nombre es obligatorio");
        }
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            throw new NombreInvalidoException("El nombre es obligatorio");
        }
        if (limpio.length() < LONGITUD_MINIMA || limpio.length() > LONGITUD_MAXIMA) {
            throw new NombreInvalidoException(
                    "El nombre debe tener entre " + LONGITUD_MINIMA + " y " + LONGITUD_MAXIMA + " caracteres");
        }
        if (!PATRON.matcher(limpio).matches()) {
            throw new NombreInvalidoException("El nombre '" + valor + "' contiene caracteres no permitidos o un formato inválido");
        }
        return new NombrePersona(capitalizar(limpio));
    }

    private static String capitalizar(String valor) {
        String[] palabras = valor.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            if (i > 0) {
                resultado.append(" ");
            }
            String palabra = palabras[i];
            String minuscula = palabra.toLowerCase();
            if (i > 0 && PREPOSICIONES.contains(minuscula)) {
                resultado.append(minuscula);
            } else {
                resultado.append(capitalizarPalabra(palabra));
            }
        }
        return resultado.toString();
    }

    private static String capitalizarPalabra(String palabra) {
        String[] partes = palabra.split("-");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < partes.length; i++) {
            if (i > 0) {
                resultado.append("-");
            }
            String parte = partes[i];
            resultado.append(Character.toUpperCase(parte.charAt(0))).append(parte.substring(1).toLowerCase());
        }
        return resultado.toString();
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NombrePersona other)) {
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
