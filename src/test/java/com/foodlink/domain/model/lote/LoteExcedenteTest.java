package com.foodlink.domain.model.lote;

import com.foodlink.domain.event.EntregaConfirmada;
import com.foodlink.domain.event.LotePublicado;
import com.foodlink.domain.event.LoteReservado;
import com.foodlink.domain.model.lote.exception.LoteNoDisponibleException;
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

class LoteExcedenteTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();

    private FechaCaducidad fechaCaducidadValida() {
        return FechaCaducidad.de(LocalDateTime.now().plusDays(2));
    }

    private LoteExcedente loteParaVenta() {
        Dinero precioMercado = Dinero.de(new BigDecimal("100"), "USD");
        Dinero precio = Dinero.de(new BigDecimal("30"), "USD");
        return FabricaLote.crearParaVenta(COMERCIO_ID, 10, precio, precioMercado, fechaCaducidadValida(),
                "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"));
    }

    private LoteExcedente loteParaDonacion() {
        return FabricaLote.crearParaDonacion(COMERCIO_ID, 10, fechaCaducidadValida(),
                "Pan del día", List.of("https://foto.com/1.jpg"));
    }

    private LoteExcedente loteEnEstado(EstadoLote estado, Modalidad modalidad) {
        return LoteExcedente.reconstituir(UUID.randomUUID(), COMERCIO_ID, modalidad, estado, 10,
                Dinero.de(new BigDecimal("30"), "USD"), null, fechaCaducidadValida(), null,
                "Descripción válida", List.of("https://foto.com/1.jpg"), null, null, null, null, null);
    }

    @Test
    void deberiaPublicarUnLoteEnBorrador() {
        LoteExcedente lote = loteParaVenta();

        lote.publicar();

        assertEquals(EstadoLote.DISPONIBLE, lote.getEstado());
    }

    @Test
    void deberiaReservarUnLoteDisponible() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();

        lote.reservar(UUID.randomUUID());

        assertEquals(EstadoLote.RESERVADO, lote.getEstado());
    }

    @Test
    void deberiaCancelarLaReservaDeUnLoteReservado() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();
        lote.reservar(UUID.randomUUID());

        lote.cancelarReserva("El beneficiario no pudo retirar");

        assertEquals(EstadoLote.DISPONIBLE, lote.getEstado());
    }

    @Test
    void deberiaRegistrarVentaDeUnLoteReservadoEnModalidadVenta() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();
        lote.reservar(UUID.randomUUID());

        lote.registrarVenta(UUID.randomUUID());

        assertEquals(EstadoLote.VENDIDO, lote.getEstado());
    }

    @Test
    void deberiaRegistrarDonacionDeUnLoteReservadoEnModalidadDonacion() {
        LoteExcedente lote = loteParaDonacion();
        lote.publicar();
        lote.reservar(UUID.randomUUID());

        lote.registrarDonacion(UUID.randomUUID());

        assertEquals(EstadoLote.DONADO, lote.getEstado());
    }

    @Test
    void deberiaConfirmarEntregaDeUnLoteVendido() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();
        lote.reservar(UUID.randomUUID());
        lote.registrarVenta(UUID.randomUUID());

        lote.confirmarEntrega(UUID.randomUUID());

        assertEquals(EstadoLote.ENTREGADO, lote.getEstado());
    }

    @Test
    void deberiaConfirmarEntregaDeUnLoteDonado() {
        LoteExcedente lote = loteParaDonacion();
        lote.publicar();
        lote.reservar(UUID.randomUUID());
        lote.registrarDonacion(UUID.randomUUID());

        lote.confirmarEntrega(UUID.randomUUID());

        assertEquals(EstadoLote.ENTREGADO, lote.getEstado());
    }

    @Test
    void deberiaExpirarUnLoteDisponible() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();

        lote.expirar();

        assertEquals(EstadoLote.EXPIRADO, lote.getEstado());
    }

    @Test
    void expirarSobreUnLoteYaExpiradoEsIdempotente() {
        LoteExcedente lote = loteEnEstado(EstadoLote.EXPIRADO, Modalidad.VENTA);

        lote.expirar();

        assertEquals(EstadoLote.EXPIRADO, lote.getEstado());
    }

    @Test
    void deberiaFallarAlRegistrarVentaDesdeDisponible() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();

        assertThrows(LoteNoDisponibleException.class, () -> lote.registrarVenta(UUID.randomUUID()));
    }

    @Test
    void deberiaFallarAlReservarUnLoteEntregado() {
        LoteExcedente lote = loteEnEstado(EstadoLote.ENTREGADO, Modalidad.VENTA);

        assertThrows(LoteNoDisponibleException.class, () -> lote.reservar(UUID.randomUUID()));
    }

    @Test
    void deberiaFallarAlPublicarUnLoteExpirado() {
        LoteExcedente lote = loteEnEstado(EstadoLote.EXPIRADO, Modalidad.VENTA);

        assertThrows(LoteNoDisponibleException.class, lote::publicar);
    }

    @Test
    void deberiaFallarAlReservarUnLoteEnBorrador() {
        LoteExcedente lote = loteParaVenta();

        assertThrows(LoteNoDisponibleException.class, () -> lote.reservar(UUID.randomUUID()));
    }

    @Test
    void publicarDeberiaEmitirLotePublicado() {
        LoteExcedente lote = loteParaVenta();

        lote.publicar();

        List<Object> eventos = lote.pullEventos();
        assertTrue(eventos.stream().anyMatch(evento -> evento instanceof LotePublicado));
    }

    @Test
    void reservarDeberiaEmitirLoteReservado() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();
        lote.pullEventos();

        lote.reservar(UUID.randomUUID());

        List<Object> eventos = lote.pullEventos();
        assertTrue(eventos.stream().anyMatch(evento -> evento instanceof LoteReservado));
    }

    @Test
    void confirmarEntregaDeberiaEmitirEntregaConfirmada() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();
        lote.reservar(UUID.randomUUID());
        lote.registrarVenta(UUID.randomUUID());
        lote.pullEventos();

        lote.confirmarEntrega(UUID.randomUUID());

        List<Object> eventos = lote.pullEventos();
        assertTrue(eventos.stream().anyMatch(evento -> evento instanceof EntregaConfirmada));
    }

    @Test
    void pullEventosDeberiaLimpiarLaListaInternaDespuesDeLlamarse() {
        LoteExcedente lote = loteParaVenta();
        lote.publicar();

        lote.pullEventos();
        List<Object> segundaLlamada = lote.pullEventos();

        assertTrue(segundaLlamada.isEmpty());
    }

    @Test
    void deberiaFallarAlPublicarConFechaCaducidadMenorA24Horas() {
        LoteExcedente lote = FabricaLote.crearParaDonacion(COMERCIO_ID, 10,
                FechaCaducidad.de(LocalDateTime.now().plusHours(10)), "Descripción válida",
                List.of("https://foto.com/1.jpg"));

        assertThrows(LoteNoDisponibleException.class, lote::publicar);
    }

    @Test
    void deberiaFallarAlPublicarSinDescripcion() {
        LoteExcedente lote = FabricaLote.crearParaDonacion(COMERCIO_ID, 10, fechaCaducidadValida(),
                "", List.of("https://foto.com/1.jpg"));

        assertThrows(LoteNoDisponibleException.class, lote::publicar);
    }

    @Test
    void deberiaFallarAlPublicarSinFotos() {
        LoteExcedente lote = FabricaLote.crearParaDonacion(COMERCIO_ID, 10, fechaCaducidadValida(),
                "Descripción válida", List.of());

        assertThrows(LoteNoDisponibleException.class, lote::publicar);
    }
}
