package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.CedulaInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CedulaEcuatorianaTest {

    @Test
    void deberiaCrearseConCedulaValidaDePichincha() {
        CedulaEcuatoriana cedula = CedulaEcuatoriana.de("1710034065");

        assertEquals("1710034065", cedula.valor());
    }

    @Test
    void deberiaCrearseConCedulaValidaDeGuayas() {
        CedulaEcuatoriana cedula = CedulaEcuatoriana.de("0912345675");

        assertEquals("0912345675", cedula.valor());
    }

    @Test
    void deberiaFallarConMenosDe10Digitos() {
        assertThrows(CedulaInvalidaException.class, () -> CedulaEcuatoriana.de("171003406"));
    }

    @Test
    void deberiaFallarConProvincia00() {
        assertThrows(CedulaInvalidaException.class, () -> CedulaEcuatoriana.de("0010034065"));
    }

    @Test
    void deberiaFallarConProvincia25() {
        assertThrows(CedulaInvalidaException.class, () -> CedulaEcuatoriana.de("2510034065"));
    }

    @Test
    void deberiaFallarConTercerDigito6() {
        assertThrows(CedulaInvalidaException.class, () -> CedulaEcuatoriana.de("1760034065"));
    }

    @Test
    void deberiaFallarConDigitoVerificadorIncorrecto() {
        assertThrows(CedulaInvalidaException.class, () -> CedulaEcuatoriana.de("1710034066"));
    }
}
