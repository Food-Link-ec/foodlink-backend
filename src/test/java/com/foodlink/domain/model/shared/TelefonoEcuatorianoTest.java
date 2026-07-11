package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.TelefonoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelefonoEcuatorianoTest {

    @Test
    void deberiaCrearseConCelularValido() {
        TelefonoEcuatoriano telefono = TelefonoEcuatoriano.de("0991234567");

        assertEquals("0991234567", telefono.valor());
    }

    @Test
    void deberiaCrearseConFijoValidoDeQuito() {
        TelefonoEcuatoriano telefono = TelefonoEcuatoriano.de("022345678");

        assertEquals("022345678", telefono.valor());
    }

    @Test
    void deberiaCrearseConFijoValidoDeGuayaquil() {
        TelefonoEcuatoriano telefono = TelefonoEcuatoriano.de("042345678");

        assertEquals("042345678", telefono.valor());
    }

    @Test
    void deberiaCrearseConCodigoDePaisYGuardarFormatoLocal() {
        TelefonoEcuatoriano telefono = TelefonoEcuatoriano.de("+593991234567");

        assertEquals("0991234567", telefono.valor());
    }

    @Test
    void deberiaFallarConNumeroDe8Digitos() {
        assertThrows(TelefonoInvalidoException.class, () -> TelefonoEcuatoriano.de("23456789"));
    }

    @Test
    void deberiaFallarSiNoEmpiezaConCero() {
        assertThrows(TelefonoInvalidoException.class, () -> TelefonoEcuatoriano.de("1991234567"));
    }
}
