package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.RucInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RucEcuatorianoTest {

    @Test
    void deberiaCrearseConRucDeSociedadPrivadaValido() {
        RucEcuatoriano ruc = RucEcuatoriano.de("1792146739001");

        assertEquals("1792146739001", ruc.valor());
    }

    @Test
    void deberiaCrearseConRucDeSociedadPublicaValido() {
        RucEcuatoriano ruc = RucEcuatoriano.de("1760001390001");

        assertEquals("1760001390001", ruc.valor());
    }

    @Test
    void deberiaCrearseConRucDePersonaNaturalValido() {
        RucEcuatoriano ruc = RucEcuatoriano.de("1710034065001");

        assertEquals("1710034065001", ruc.valor());
    }

    @Test
    void deberiaFallarConMenosDe13Digitos() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("179214673900"));
    }

    @Test
    void deberiaFallarConProvincia00() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("0092146739001"));
    }

    @Test
    void deberiaFallarConProvinciaMayorA24() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("2592146739001"));
    }

    @Test
    void deberiaFallarConTercerDigito7() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("1770000000001"));
    }

    @Test
    void deberiaFallarConTercerDigito8() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("1780000000001"));
    }

    @Test
    void deberiaFallarConDigitoVerificadorIncorrecto() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("1710034066001"));
    }

    @Test
    void deberiaFallarSiNoTerminaEn001() {
        assertThrows(RucInvalidoException.class, () -> RucEcuatoriano.de("1792146739002"));
    }
}
