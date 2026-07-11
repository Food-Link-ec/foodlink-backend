package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.RucInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class RucEcuatoriano {

    private static final Pattern PATRON = Pattern.compile("\\d{13}");
    private static final int[] COEFICIENTES_PUBLICA = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] COEFICIENTES_PRIVADA = {4, 3, 2, 7, 6, 5, 4, 3, 2};

    private final String valor;

    private RucEcuatoriano(String valor) {
        this.valor = valor;
    }

    public static RucEcuatoriano de(String valor) {
        String limpio = limpiar(valor);
        validarFormato(limpio);
        validarProvincia(limpio);
        char tercerDigito = limpio.charAt(2);
        validarTipoContribuyente(tercerDigito, limpio);
        validarDigitoVerificador(limpio, tercerDigito);
        validarEstablecimiento(limpio);
        return new RucEcuatoriano(limpio);
    }

    private static String limpiar(String valor) {
        if (valor == null) {
            throw new RucInvalidoException("El RUC es obligatorio");
        }
        return valor.replaceAll("[\\s-]", "");
    }

    private static void validarFormato(String valor) {
        if (!PATRON.matcher(valor).matches()) {
            throw new RucInvalidoException("El RUC '" + valor + "' debe tener exactamente 13 dígitos numéricos");
        }
    }

    private static void validarProvincia(String valor) {
        int provincia = Integer.parseInt(valor.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            throw new RucInvalidoException("El RUC '" + valor + "' tiene un código de provincia inválido");
        }
    }

    private static void validarTipoContribuyente(char tercerDigito, String valor) {
        if (tercerDigito == '7' || tercerDigito == '8') {
            throw new RucInvalidoException("El RUC '" + valor + "' tiene un tercer dígito inválido");
        }
    }

    private static void validarDigitoVerificador(String valor, char tercerDigito) {
        int digito = Character.getNumericValue(tercerDigito);
        if (digito <= 5) {
            validarModulo10(valor);
        } else if (digito == 6) {
            validarModulo11(valor, COEFICIENTES_PUBLICA, 8);
        } else {
            validarModulo11(valor, COEFICIENTES_PRIVADA, 9);
        }
    }

    private static void validarModulo10(String valor) {
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
            throw new RucInvalidoException("El RUC '" + valor + "' tiene un dígito verificador inválido");
        }
    }

    private static void validarModulo11(String valor, int[] coeficientes, int posicionVerificador) {
        int suma = 0;
        for (int i = 0; i < coeficientes.length; i++) {
            suma += Character.getNumericValue(valor.charAt(i)) * coeficientes[i];
        }
        int residuo = suma % 11;
        if (residuo == 1) {
            throw new RucInvalidoException("El RUC '" + valor + "' tiene un dígito verificador inválido");
        }
        int verificador = residuo == 0 ? 0 : 11 - residuo;
        int digitoEsperado = Character.getNumericValue(valor.charAt(posicionVerificador));
        if (verificador != digitoEsperado) {
            throw new RucInvalidoException("El RUC '" + valor + "' tiene un dígito verificador inválido");
        }
    }

    private static void validarEstablecimiento(String valor) {
        if (!valor.substring(10, 13).equals("001")) {
            throw new RucInvalidoException("El RUC '" + valor + "' debe terminar en 001 para el establecimiento principal");
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
        if (!(o instanceof RucEcuatoriano other)) {
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
