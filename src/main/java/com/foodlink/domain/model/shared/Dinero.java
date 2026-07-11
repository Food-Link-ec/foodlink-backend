package com.foodlink.domain.model.shared;

import java.math.BigDecimal;
import java.util.Objects;

public final class Dinero {

    private static final BigDecimal LIMITE_PORCENTAJE_VENTA = new BigDecimal("0.40");

    private final BigDecimal monto;
    private final String moneda;

    private Dinero(BigDecimal monto, String moneda) {
        this.monto = monto;
        this.moneda = moneda;
    }

    public static Dinero de(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (moneda == null || moneda.isBlank()) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }
        return new Dinero(monto, moneda);
    }

    public static Dinero cero() {
        return new Dinero(BigDecimal.ZERO, "USD");
    }

    public boolean esCero() {
        return monto.compareTo(BigDecimal.ZERO) == 0;
    }

    public void validarPrecioVenta(Dinero precioMercado) {
        BigDecimal limite = precioMercado.monto.multiply(LIMITE_PORCENTAJE_VENTA);
        if (monto.compareTo(limite) > 0) {
            throw new IllegalArgumentException("El precio de venta no puede superar el 40% del precio de mercado");
        }
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getMoneda() {
        return moneda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dinero other)) {
            return false;
        }
        return monto.compareTo(other.monto) == 0 && moneda.equals(other.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto.stripTrailingZeros(), moneda);
    }

    @Override
    public String toString() {
        return monto + " " + moneda;
    }
}
