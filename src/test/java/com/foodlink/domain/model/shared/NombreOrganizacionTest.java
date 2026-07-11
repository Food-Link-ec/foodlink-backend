package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.NombreInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NombreOrganizacionTest {

    @Test
    void deberiaCrearseConRazonSocialValida() {
        NombreOrganizacion nombre = NombreOrganizacion.de("Fundación Manos Unidas & Co. (Ecuador)");

        assertEquals("Fundación Manos Unidas & Co. (Ecuador)", nombre.valor());
    }

    @Test
    void deberiaFallarConNombreVacio() {
        assertThrows(NombreInvalidoException.class, () -> NombreOrganizacion.de(""));
    }

    @Test
    void deberiaFallarConCaracteresNoPermitidos() {
        assertThrows(NombreInvalidoException.class, () -> NombreOrganizacion.de("Empresa#Invalida"));
    }
}
