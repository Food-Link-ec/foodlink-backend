package com.foodlink.domain.model.shared;

import com.foodlink.domain.model.shared.exception.PinInvalidoException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PinRetiroTest {

    private static final String ALFABETO_PERMITIDO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    @Test
    void generarDeberiaRetornarPinDeCincoCaracteres() {
        PinRetiro pin = PinRetiro.generar();

        assertEquals(5, pin.valor().length());
    }

    @Test
    void generarDeberiaSoloContenerCaracteresDelAlfabetoPermitido() {
        PinRetiro pin = PinRetiro.generar();

        for (char caracter : pin.valor().toCharArray()) {
            assertTrue(ALFABETO_PERMITIDO.indexOf(caracter) >= 0);
        }
    }

    @Test
    void generarEnCienLlamadasDeberiaGenerarAlMenosNoventaValoresDistintos() {
        Set<String> valores = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            valores.add(PinRetiro.generar().valor());
        }

        assertTrue(valores.size() >= 90);
    }

    @Test
    void deDeberiaCrearsePinValidoCorrectamente() {
        PinRetiro pin = PinRetiro.de("ABC12");

        assertEquals("ABC12", pin.valor());
    }

    @Test
    void deDeberiaNormalizarMinusculasAMayusculas() {
        PinRetiro pin = PinRetiro.de("abc12");

        assertEquals("ABC12", pin.valor());
    }

    @Test
    void deDeberiaFallarConMenosDeCincoCaracteres() {
        assertThrows(PinInvalidoException.class, () -> PinRetiro.de("AB"));
    }

    @Test
    void deDeberiaFallarConCaracteresNoAlfanumericos() {
        assertThrows(PinInvalidoException.class, () -> PinRetiro.de("AB@12"));
    }

    @Test
    void generarQrDataDeberiaRetornarFormatoEsperado() {
        PinRetiro pin = PinRetiro.de("ABC12");
        UUID loteId = UUID.randomUUID();

        String qrData = pin.generarQrData(loteId);

        assertEquals("FOODLINK:RETIRO:" + loteId + ":ABC12", qrData);
    }
}