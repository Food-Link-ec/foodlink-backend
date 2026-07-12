package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.CancelarReservaRequest;
import com.foodlink.application.dto.request.ReservarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.lote.exception.LoteNoDisponibleException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservarLoteUseCaseTest {

    @Mock
    private IRepositorioLote repositorioLote;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReservarLoteUseCaseImpl useCase;

    private void mockGuardarEcheandoElMismoLote() {
        when(repositorioLote.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private LoteExcedente loteEnEstado(UUID id, EstadoLote estado) {
        return LoteExcedente.reconstituir(id, UUID.randomUUID(), Modalidad.VENTA, estado, 10,
                Dinero.de(new BigDecimal("30"), "USD"), FechaCaducidad.de(LocalDateTime.now().plusDays(3)),
                LocalDateTime.now(), "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"), null, null,
                null, null);
    }

    @Test
    void reservarConLoteDisponibleDeberiaRetornarLoteResponseConEstadoReservado() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.DISPONIBLE);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID());

        assertEquals(EstadoLote.RESERVADO.name(), response.estado());
    }

    @Test
    void reservarConLoteNoEncontradoDeberiaLanzarIllegalArgumentException() {
        UUID loteId = UUID.randomUUID();
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID()));
    }

    @Test
    void reservarConLoteExpiradoDeberiaLanzarLoteNoDisponibleException() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.EXPIRADO);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));

        assertThrows(LoteNoDisponibleException.class,
                () -> useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID()));
    }

    @Test
    void reservarConLoteYaReservadoDeberiaLanzarLoteNoDisponibleException() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.RESERVADO);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));

        assertThrows(LoteNoDisponibleException.class,
                () -> useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID()));
    }

    @Test
    void reservarDeberiaLlamarGuardarExactamenteUnaVez() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.DISPONIBLE);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));
        mockGuardarEcheandoElMismoLote();

        useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID());

        verify(repositorioLote, times(1)).guardar(any());
    }

    @Test
    void reservarDeberiaPublicarEventosViaEventPublisher() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.DISPONIBLE);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));
        mockGuardarEcheandoElMismoLote();

        useCase.reservar(new ReservarLoteRequest(loteId), UUID.randomUUID());

        verify(eventPublisher, atLeastOnce()).publishEvent(any(Object.class));
    }

    @Test
    void cancelarReservaConLoteReservadoDeberiaRetornarLoteResponseConEstadoDisponible() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.RESERVADO);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.cancelarReserva(new CancelarReservaRequest(loteId, "Ya no lo necesito"));

        assertEquals(EstadoLote.DISPONIBLE.name(), response.estado());
    }

    @Test
    void cancelarReservaConLoteNoEncontradoDeberiaLanzarIllegalArgumentException() {
        UUID loteId = UUID.randomUUID();
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> useCase.cancelarReserva(new CancelarReservaRequest(loteId, "motivo")));
    }

    @Test
    void cancelarReservaConLoteDisponibleDeberiaLanzarLoteNoDisponibleException() {
        UUID loteId = UUID.randomUUID();
        LoteExcedente lote = loteEnEstado(loteId, EstadoLote.DISPONIBLE);
        when(repositorioLote.buscarPorId(loteId)).thenReturn(Optional.of(lote));

        assertThrows(LoteNoDisponibleException.class,
                () -> useCase.cancelarReserva(new CancelarReservaRequest(loteId, "motivo")));
    }
}