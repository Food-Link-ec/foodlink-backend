package com.foodlink.infrastructure.adapter.output.ia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TesseractOcrAdapterTest {

    private final TesseractOcrAdapter adapter = new TesseractOcrAdapter("");

    @Test
    void deberiaDetectarFechaConFormatoDiaMesAnioSeparadoPorBarras() {
        String fecha = adapter.extraerFecha("Fecha de caducidad: 15/08/2026 - lote A");
        assertEquals("15/08/2026", fecha);
    }

    @Test
    void deberiaDetectarFechaConFormatoDiaMesAnioSeparadoPorGuiones() {
        String fecha = adapter.extraerFecha("Caduca 15-08-2026");
        assertEquals("15-08-2026", fecha);
    }

    @Test
    void deberiaDetectarFechaConFormatoAnioMesDia() {
        String fecha = adapter.extraerFecha("Vence: 2026-08-15");
        assertEquals("2026-08-15", fecha);
    }

    @Test
    void deberiaRetornarNullCuandoElTextoNoContieneFecha() {
        String fecha = adapter.extraerFecha("Producto sin informacion de fecha visible");
        assertNull(fecha);
    }

    @Test
    void deberiaRetornarNullCuandoElTextoEsNull() {
        assertNull(adapter.extraerFecha(null));
    }

    @Test
    void deberiaRetornarNullCuandoElTextoEstaEnBlanco() {
        assertNull(adapter.extraerFecha("   "));
    }

    @Test
    void resultadoDeberiaExponerLosValoresCorrectosSegunElFactoryUtilizado() {
        TesseractOcrAdapter.TesseractResultado exitoso = TesseractOcrAdapter.TesseractResultado.exitoso("10/10/2026", "texto completo");
        assertTrue(exitoso.exitoso());
        assertTrue(exitoso.tieneFecha());
        assertEquals("10/10/2026", exitoso.fecha());
        assertEquals("texto completo", exitoso.textoCompleto());

        TesseractOcrAdapter.TesseractResultado sinFecha = TesseractOcrAdapter.TesseractResultado.sinFecha("texto sin fecha");
        assertTrue(sinFecha.exitoso());
        assertFalse(sinFecha.tieneFecha());
        assertNull(sinFecha.fecha());
        assertEquals("texto sin fecha", sinFecha.textoCompleto());

        TesseractOcrAdapter.TesseractResultado fallido = TesseractOcrAdapter.TesseractResultado.fallido();
        assertFalse(fallido.exitoso());
        assertFalse(fallido.tieneFecha());
        assertNull(fallido.fecha());
        assertNull(fallido.textoCompleto());
    }
}
