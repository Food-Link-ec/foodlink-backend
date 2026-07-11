package com.foodlink.domain.model.comprador;

import com.foodlink.domain.model.comprador.exception.CompradorInvalidoException;
import com.foodlink.domain.model.shared.exception.CedulaInvalidaException;
import com.foodlink.domain.model.shared.exception.NombreInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompradorTest {

    private static final String CEDULA_VALIDA = "1710034065";

    @Test
    void deberiaRegistrarCompradorConCedulaValida() {
        Comprador comprador = Comprador.registrar(CEDULA_VALIDA, "Juan", "Pérez",
                "juan.perez@mail.com", "0991234567");

        assertNotNull(comprador.getId());
        assertEquals(CEDULA_VALIDA, comprador.getCedula().valor());
        assertTrue(comprador.estaActivo());
        assertNotNull(comprador.getFechaRegistro());
    }

    @Test
    void deberiaFallarConCedulaDeLongitudIncorrecta() {
        assertThrows(CedulaInvalidaException.class, () ->
                Comprador.registrar("171003406", "Juan", "Pérez", "juan.perez@mail.com", "0991234567"));
    }

    @Test
    void deberiaFallarConCedulaConProvinciaInvalida() {
        assertThrows(CedulaInvalidaException.class, () ->
                Comprador.registrar("9910034065", "Juan", "Pérez", "juan.perez@mail.com", "0991234567"));
    }

    @Test
    void deberiaFallarConCedulaConDigitoVerificadorIncorrecto() {
        assertThrows(CedulaInvalidaException.class, () ->
                Comprador.registrar("1710034066", "Juan", "Pérez", "juan.perez@mail.com", "0991234567"));
    }

    @Test
    void deberiaFallarAlRegistrarConNombreVacio() {
        assertThrows(NombreInvalidoException.class, () ->
                Comprador.registrar(CEDULA_VALIDA, "", "Pérez", "juan.perez@mail.com", "0991234567"));
    }

    @Test
    void deberiaDesactivarseExitosamenteUnCompradorActivo() {
        Comprador comprador = Comprador.registrar(CEDULA_VALIDA, "Juan", "Pérez",
                "juan.perez@mail.com", "0991234567");

        comprador.desactivar();

        assertFalse(comprador.estaActivo());
    }

    @Test
    void deberiaFallarAlDesactivarUnCompradorYaInactivo() {
        Comprador comprador = Comprador.registrar(CEDULA_VALIDA, "Juan", "Pérez",
                "juan.perez@mail.com", "0991234567");
        comprador.desactivar();

        assertThrows(CompradorInvalidoException.class, comprador::desactivar);
    }

    @Test
    void deberiaActivarseExitosamenteUnCompradorInactivo() {
        Comprador comprador = Comprador.registrar(CEDULA_VALIDA, "Juan", "Pérez",
                "juan.perez@mail.com", "0991234567");
        comprador.desactivar();

        comprador.activar();

        assertTrue(comprador.estaActivo());
    }
}