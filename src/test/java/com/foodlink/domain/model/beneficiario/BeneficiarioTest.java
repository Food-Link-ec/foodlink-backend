package com.foodlink.domain.model.beneficiario;

import com.foodlink.domain.model.beneficiario.exception.BeneficiarioInvalidoException;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.model.shared.exception.NombreInvalidoException;
import com.foodlink.domain.model.shared.exception.RucInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeneficiarioTest {

    private static final String RUC_VALIDO = "1792146739001";

    private Direccion direccionValida() {
        return new Direccion("Av. 6 de Diciembre N32-100", "Quito", "Pichincha", "Junto al parque", -0.180653, -78.467834);
    }

    @Test
    void deberiaRegistrarBeneficiarioValido() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());

        assertNotNull(beneficiario.getId());
        assertEquals("Fundación Manos Unidas", beneficiario.getNombre().valor());
        assertEquals(EstadoVerificacion.PENDIENTE, beneficiario.getEstadoVerificacion());
        assertNotNull(beneficiario.getFechaRegistro());
    }

    @Test
    void deberiaFallarAlRegistrarConNombreVacio() {
        assertThrows(NombreInvalidoException.class, () ->
                Beneficiario.registrar("", RUC_VALIDO, "contacto@manosunidas.org", "0991234567", direccionValida()));
    }

    @Test
    void deberiaFallarAlRegistrarConRucInvalido() {
        assertThrows(RucInvalidoException.class, () ->
                Beneficiario.registrar("Fundación Manos Unidas", "12345", "contacto@manosunidas.org",
                        "0991234567", direccionValida()));
    }

    @Test
    void deberiaVerificarseExitosamenteDesdePendiente() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());

        beneficiario.verificar();

        assertEquals(EstadoVerificacion.VERIFICADO, beneficiario.getEstadoVerificacion());
        assertTrue(beneficiario.estaVerificado());
    }

    @Test
    void deberiaFallarAlVerificarUnBeneficiarioYaVerificado() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());
        beneficiario.verificar();

        assertThrows(BeneficiarioInvalidoException.class, beneficiario::verificar);
    }

    @Test
    void deberiaRechazarseExitosamenteDesdePendiente() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());

        beneficiario.rechazar("Documentación incompleta");

        assertEquals(EstadoVerificacion.RECHAZADO, beneficiario.getEstadoVerificacion());
    }

    @Test
    void puedeRecibirDonacionesSoloSiEstaVerificado() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Manos Unidas", RUC_VALIDO,
                "contacto@manosunidas.org", "0991234567", direccionValida());

        assertFalse(beneficiario.puedeRecibirDonaciones());

        beneficiario.verificar();

        assertTrue(beneficiario.puedeRecibirDonaciones());
    }
}