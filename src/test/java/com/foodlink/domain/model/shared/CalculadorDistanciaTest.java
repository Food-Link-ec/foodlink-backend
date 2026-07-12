package com.foodlink.domain.model.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculadorDistanciaTest {

    private static final double LATITUD_LA_CAROLINA = -0.1807;
    private static final double LONGITUD_LA_CAROLINA = -78.4897;
    private static final double LATITUD_CENTRO_HISTORICO = -0.2201;
    private static final double LONGITUD_CENTRO_HISTORICO = -78.5123;

    @Test
    void calcularKmEntreLaMismaUbicacionDeberiaSerCero() {
        double distancia = CalculadorDistancia.calcularKm(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA);

        assertEquals(0.0, distancia);
    }

    @Test
    void calcularKmEntreQuitoCentroYParqueLaCarolinaDeberiaSerAproximadamenteCincoKm() {
        double distancia = CalculadorDistancia.calcularKm(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_CENTRO_HISTORICO, LONGITUD_CENTRO_HISTORICO);

        assertEquals(5.05, distancia, 0.1);
    }

    @Test
    void radioSeisKmDesdeLaCarolinaDeberiaIncluirElHornoQuiteno() {
        boolean dentro = CalculadorDistancia.estaDentroDeRadio(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_CENTRO_HISTORICO, LONGITUD_CENTRO_HISTORICO, 6.0);

        assertTrue(dentro);
    }

    @Test
    void radioUnKmDesdeLaCarolinaNoDeberiaIncluirElHornoQuiteno() {
        boolean dentro = CalculadorDistancia.estaDentroDeRadio(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_CENTRO_HISTORICO, LONGITUD_CENTRO_HISTORICO, 1.0);

        assertFalse(dentro);
    }

    @Test
    void estaDentroDeRadioConRadioSuficienteDeberiaRetornarTrue() {
        boolean dentro = CalculadorDistancia.estaDentroDeRadio(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_CENTRO_HISTORICO, LONGITUD_CENTRO_HISTORICO, 10.0);

        assertTrue(dentro);
    }

    @Test
    void estaDentroDeRadioConRadioInsuficienteDeberiaRetornarFalse() {
        boolean dentro = CalculadorDistancia.estaDentroDeRadio(
                LATITUD_LA_CAROLINA, LONGITUD_LA_CAROLINA, LATITUD_CENTRO_HISTORICO, LONGITUD_CENTRO_HISTORICO, 0.5);

        assertFalse(dentro);
    }
}
