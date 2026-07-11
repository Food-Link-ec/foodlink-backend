package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.CedulaInvalidaException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class CedulaEcuatoriana {

    private static final Pattern PATRON = Pattern.compile("\\d{10}");

    private final String valor;

    private CedulaEcuatoriana(String valor) {
        this.valor = valor;
    }

    public static CedulaEcuatoriana de(String valor) {
        String limpio = limpiar(valor);
        validarFormato(limpio);
        validarProvincia(limpio);
        validarTercerDigito(limpio);
        validarDigitoVerificador(limpio);
        return new CedulaEcuatoriana(limpio);
    }

    private static String limpiar(String valor) {
        if (valor == null) {
            throw new CedulaInvalidaException("La cédula es obligatoria");
        }
        return valor.replaceAll("[\\s-]", "");
    }

    private static void validarFormato(String valor) {
        if (!PATRON.matcher(valor).matches()) {
            throw new CedulaInvalidaException("La cédula '" + valor + "' debe tener exactamente 10 dígitos numéricos");
        }
    }

    private static void validarProvincia(String valor) {
        int provincia = Integer.parseInt(valor.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            throw new CedulaInvalidaException("La cédula '" + valor + "' tiene un código de provincia inválido");
        }
    }

    private static void validarTercerDigito(String valor) {
        int tercerDigito = Character.getNumericValue(valor.charAt(2));
        if (tercerDigito >= 6) {
            throw new CedulaInvalidaException("La cédula '" + valor + "' tiene un tercer dígito inválido");
        }
    }

    private static void validarDigitoVerificador(String valor) {
        int suma = 0;
        for (int posicion = 0; posicion < 9; posicion++) {
            int digito = Character.getNumericValue(valor.charAt(posicion));
            if (posicion % 2 == 0) {
                int doble = digito * 2;
                if (doble >= 10) {
                    doble -= 9;
                }
                suma += doble;
            } else {
                suma += digito;
            }
        }
        int digitoVerificador = Character.getNumericValue(valor.charAt(9));
        if ((suma + digitoVerificador) % 10 != 0) {
            throw new CedulaInvalidaException("La cédula '" + valor + "' tiene un dígito verificador inválido");
        }
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CedulaEcuatoriana other)) {
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
