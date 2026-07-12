package com.foodlink.infrastructure.adapter.input.scheduler;

import com.foodlink.domain.event.ReservaCancelada;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.FabricaLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoteExpirationJobTest {

    @Mock
    private IRepositorioLote repositorioLote;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private LoteExpirationJob job;

    private LoteExcedente loteReservadoExpirado() {
        LoteExcedente lote = FabricaLote.crearParaVenta(UUID.randomUUID(), 10,
                Dinero.de(new BigDecimal("30"), "USD"), Dinero.de(new BigDecimal("100"), "USD"),
                FechaCaducidad.de(LocalDateTime.now().plusDays(3)), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"));
        lote.publicar();
        lote.reservar(UUID.randomUUID());
        lote.setInicioReservaParaTest(LocalDateTime.now().minusMinutes(45));
        return lote;
    }

    private LoteExcedente loteDisponibleCaducado() {
        LoteExcedente lote = FabricaLote.crearParaDonacion(UUID.randomUUID(), 10,
                FechaCaducidad.de(LocalDateTime.now().plusDays(3)), "Pan del día",
                List.of("https://foto.com/1.jpg"));
        lote.publicar();
        return lote;
    }

    private LoteExcedente loteReservadoCaducado() {
        LoteExcedente lote = loteReservadoExpirado();
        return lote;
    }

    @Test
    void cancelarReservasExpiradasConListaVaciaNoDeberiaLlamarGuardarNiPublishEvent() {
        when(repositorioLote.buscarReservasExpiradas(any())).thenReturn(List.of());

        job.cancelarReservasExpiradas();

        verify(repositorioLote, never()).guardar(any());
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }

    @Test
    void cancelarReservasExpiradasConUnLoteExpiradoDeberiaLlamarCancelarReservaYGuardar() {
        LoteExcedente lote = loteReservadoExpirado();
        when(repositorioLote.buscarReservasExpiradas(any())).thenReturn(List.of(lote));

        job.cancelarReservasExpiradas();

        verify(repositorioLote, times(1)).guardar(lote);
    }

    @Test
    void cancelarReservasExpiradasConUnLoteExpiradoDeberiaPasarAEstadoDisponible() {
        LoteExcedente lote = loteReservadoExpirado();
        when(repositorioLote.buscarReservasExpiradas(any())).thenReturn(List.of(lote));

        job.cancelarReservasExpiradas();

        assertEquals(EstadoLote.DISPONIBLE, lote.getEstado());
    }

    @Test
    void cancelarReservasExpiradasConUnLoteExpiradoDeberiaPublicarReservaCancelada() {
        LoteExcedente lote = loteReservadoExpirado();
        when(repositorioLote.buscarReservasExpiradas(any())).thenReturn(List.of(lote));

        job.cancelarReservasExpiradas();

        verify(eventPublisher, atLeastOnce()).publishEvent(any(ReservaCancelada.class));
    }

    @Test
    void cancelarReservasExpiradasConDosLotesDeberiaLlamarGuardarDosVeces() {
        LoteExcedente lote1 = loteReservadoExpirado();
        LoteExcedente lote2 = loteReservadoExpirado();
        when(repositorioLote.buscarReservasExpiradas(any())).thenReturn(List.of(lote1, lote2));

        job.cancelarReservasExpiradas();

        verify(repositorioLote, times(2)).guardar(any());
    }

    @Test
    void expirarLotesCaducadosConListaVaciaNoDeberiaLlamarGuardar() {
        when(repositorioLote.buscarLotesCaducados()).thenReturn(List.of());

        job.expirarLotesCaducados();

        verify(repositorioLote, never()).guardar(any());
    }

    @Test
    void expirarLotesCaducadosConLoteDisponibleDeberiaPasarAEstadoExpirado() {
        LoteExcedente lote = loteDisponibleCaducado();
        when(repositorioLote.buscarLotesCaducados()).thenReturn(List.of(lote));

        job.expirarLotesCaducados();

        assertEquals(EstadoLote.EXPIRADO, lote.getEstado());
    }

    @Test
    void expirarLotesCaducadosConLoteDisponibleDeberiaLlamarGuardarUnaVez() {
        LoteExcedente lote = loteDisponibleCaducado();
        when(repositorioLote.buscarLotesCaducados()).thenReturn(List.of(lote));

        job.expirarLotesCaducados();

        verify(repositorioLote, times(1)).guardar(lote);
    }

    @Test
    void expirarLotesCaducadosConLoteReservadoDeberiaPasarAEstadoExpirado() {
        LoteExcedente lote = loteReservadoCaducado();
        when(repositorioLote.buscarLotesCaducados()).thenReturn(List.of(lote));

        job.expirarLotesCaducados();

        assertEquals(EstadoLote.EXPIRADO, lote.getEstado());
    }

    @Test
    void expirarLotesCaducadosConDosLotesDeberiaLlamarGuardarDosVeces() {
        LoteExcedente lote1 = loteDisponibleCaducado();
        LoteExcedente lote2 = loteDisponibleCaducado();
        when(repositorioLote.buscarLotesCaducados()).thenReturn(List.of(lote1, lote2));

        job.expirarLotesCaducados();

        verify(repositorioLote, times(2)).guardar(any());
    }
}
