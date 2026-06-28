package com.foodlink.domain.model.comercio;

import com.foodlink.domain.model.comercio.exception.ComercioInvalidoException;
import com.foodlink.domain.model.comercio.exception.RucInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComercioTest {

    private static final String RUC_VALIDO = "1792146739001";

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", -0.180653, -78.467834);
    }

    @Test
    void deberiaCrearComercioValido() {
        Comercio comercio = Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());

        assertNotNull(comercio.getId());
        assertEquals(RUC_VALIDO, comercio.getRuc());
        assertEquals("Supermercado El Ahorro", comercio.getNombre());
        assertEquals(EstadoComercio.PENDIENTE_VERIFICACION, comercio.getEstado());
        assertNotNull(comercio.getFechaRegistro());
    }

    @Test
    void deberiaFallarAlCrearConRucInvalido() {
        assertThrows(RucInvalidoException.class, () ->
                Comercio.crear("12345", "Supermercado El Ahorro", "0991234567",
                        "contacto@elahorro.com", direccionValida()));
    }

    @Test
    void deberiaFallarAlCrearConNombreVacio() {
        assertThrows(ComercioInvalidoException.class, () ->
                Comercio.crear(RUC_VALIDO, "", "0991234567",
                        "contacto@elahorro.com", direccionValida()));
    }

    @Test
    void deberiaVerificarseExitosamenteDesdePendiente() {
        Comercio comercio = Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());

        comercio.verificar();

        assertEquals(EstadoComercio.VERIFICADO, comercio.getEstado());
        assertTrue(comercio.estaVerificado());
    }

    @Test
    void deberiaFallarAlVerificarUnComercioYaVerificado() {
        Comercio comercio = Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
        comercio.verificar();

        assertThrows(ComercioInvalidoException.class, comercio::verificar);
    }

    @Test
    void deberiaSuspenderseExitosamenteDesdeVerificado() {
        Comercio comercio = Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
        comercio.verificar();

        comercio.suspender();

        assertEquals(EstadoComercio.SUSPENDIDO, comercio.getEstado());
        assertFalse(comercio.estaActivo());
    }

    @Test
    void deberiaFallarAlSuspenderDesdePendienteVerificacion() {
        Comercio comercio = Comercio.crear(RUC_VALIDO, "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());

        assertThrows(ComercioInvalidoException.class, comercio::suspender);
    }
}