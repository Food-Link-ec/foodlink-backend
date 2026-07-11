package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.PinInvalidoException;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public final class PinRetiro {

    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LONGITUD = 5;
    private static final Pattern PATRON = Pattern.compile("[A-Z0-9]{5}");
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private final String valor;

    private PinRetiro(String valor) {
        this.valor = valor;
    }

    public static PinRetiro generar() {
        StringBuilder builder = new StringBuilder(LONGITUD);
        for (int i = 0; i < LONGITUD; i++) {
            builder.append(ALFABETO.charAt(ALEATORIO.nextInt(ALFABETO.length())));
        }
        return new PinRetiro(builder.toString());
    }

    public static PinRetiro de(String valor) {
        if (valor == null) {
            throw new PinInvalidoException("El PIN es obligatorio");
        }
        String normalizado = valor.trim().toUpperCase();
        if (!PATRON.matcher(normalizado).matches()) {
            throw new PinInvalidoException("El PIN '" + valor + "' debe tener exactamente 5 caracteres alfanuméricos");
        }
        return new PinRetiro(normalizado);
    }

    public String valor() {
        return valor;
    }

    public String generarQrData(UUID loteId) {
        return "FOODLINK:RETIRO:" + loteId.toString() + ":" + valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PinRetiro other)) {
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