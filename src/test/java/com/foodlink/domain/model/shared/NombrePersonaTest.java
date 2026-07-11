package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.NombreInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NombrePersonaTest {

    @Test
    void deberiaCrearseConNombreValidoSimple() {
        NombrePersona nombre = NombrePersona.de("juan");

        assertEquals("Juan", nombre.valor());
    }

    @Test
    void deberiaCrearseConPreposicionEnMinuscula() {
        NombrePersona nombre = NombrePersona.de("juan de la cruz");

        assertEquals("Juan de la Cruz", nombre.valor());
    }

    @Test
    void deberiaFallarConNombreVacio() {
        assertThrows(NombreInvalidoException.class, () -> NombrePersona.de(""));
    }

    @Test
    void deberiaFallarConNumeros() {
        assertThrows(NombreInvalidoException.class, () -> NombrePersona.de("Juan123"));
    }

    @Test
    void deberiaFallarConUnSoloCaracter() {
        assertThrows(NombreInvalidoException.class, () -> NombrePersona.de("A"));
    }
}
