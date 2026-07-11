package com.foodlink.domain.model.lote;

import com.foodlink.domain.event.LoteRegistrado;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FabricaLoteTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();

    private FechaCaducidad fechaCaducidadValida() {
        return FechaCaducidad.de(LocalDateTime.now().plusDays(2));
    }

    @Test
    void crearParaVentaDeberiaCrearLoteEnBorradorConModalidadVentaYEmitirEvento() {
        Dinero precioMercado = Dinero.de(new BigDecimal("100"), "USD");
        Dinero precio = Dinero.de(new BigDecimal("30"), "USD");

        LoteExcedente lote = FabricaLote.crearParaVenta(COMERCIO_ID, 10, precio, precioMercado,
                fechaCaducidadValida(), "Frutas y verduras", List.of("https://foto.com/1.jpg"));

        assertEquals(EstadoLote.BORRADOR, lote.getEstado());
        assertEquals(Modalidad.VENTA, lote.getModalidad());
        List<Object> eventos = lote.pullEventos();
        assertTrue(eventos.stream().anyMatch(evento -> evento instanceof LoteRegistrado));
    }

    @Test
    void crearParaDonacionDeberiaCrearLoteConPrecioCeroYModalidadDonacion() {
        LoteExcedente lote = FabricaLote.crearParaDonacion(COMERCIO_ID, 10, fechaCaducidadValida(),
                "Pan del día", List.of("https://foto.com/1.jpg"));

        assertTrue(lote.getPrecio().esCero());
        assertEquals(Modalidad.DONACION, lote.getModalidad());
    }

    @Test
    void crearParaRetiroDirectoDeberiaCrearLoteConPrecioCeroYModalidadRetiroDirecto() {
        LoteExcedente lote = FabricaLote.crearParaRetiroDirecto(COMERCIO_ID, 10, fechaCaducidadValida(),
                "Excedente variado", List.of("https://foto.com/1.jpg"));

        assertTrue(lote.getPrecio().esCero());
        assertEquals(Modalidad.RETIRO_DIRECTO, lote.getModalidad());
    }

    @Test
    void crearParaVentaConPrecioMayorAl40PorcientoDelMercadoDeberiaLanzarExcepcion() {
        Dinero precioMercado = Dinero.de(new BigDecimal("100"), "USD");
        Dinero precio = Dinero.de(new BigDecimal("50"), "USD");

        assertThrows(IllegalArgumentException.class, () -> FabricaLote.crearParaVenta(COMERCIO_ID, 10, precio,
                precioMercado, fechaCaducidadValida(), "Frutas y verduras", List.of("https://foto.com/1.jpg")));
    }

    @Test
    void crearParaVentaConCantidadKgMenorOIgualACeroDeberiaLanzarIllegalArgumentException() {
        Dinero precioMercado = Dinero.de(new BigDecimal("100"), "USD");
        Dinero precio = Dinero.de(new BigDecimal("30"), "USD");

        assertThrows(IllegalArgumentException.class, () -> FabricaLote.crearParaVenta(COMERCIO_ID, 0, precio,
                precioMercado, fechaCaducidadValida(), "Frutas y verduras", List.of("https://foto.com/1.jpg")));
    }

    @Test
    void crearParaDonacionConCantidadKgMenorOIgualACeroDeberiaLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> FabricaLote.crearParaDonacion(COMERCIO_ID, -5,
                fechaCaducidadValida(), "Pan del día", List.of("https://foto.com/1.jpg")));
    }
}
