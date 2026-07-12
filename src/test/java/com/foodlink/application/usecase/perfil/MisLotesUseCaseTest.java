package com.foodlink.application.usecase.perfil;

import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MisLotesUseCaseTest {

    @Mock
    private IRepositorioLote repositorioLote;

    @InjectMocks
    private MisLotesUseCaseImpl useCase;

    private LoteExcedente loteEnEstado(UUID comercioId, EstadoLote estado, UUID beneficiarioReservaId) {
        return LoteExcedente.reconstituir(UUID.randomUUID(), comercioId, Modalidad.VENTA, estado, 10,
                Dinero.de(new BigDecimal("30"), "USD"), FechaCaducidad.de(LocalDateTime.now().plusDays(3)),
                LocalDateTime.now(), "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"),
                beneficiarioReservaId, LocalDateTime.now(), null, null, null);
    }

    @Test
    void obtenerMisLotesDeberiaLlamarBuscarPorComercioConElComercioIdCorrecto() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioLote.buscarPorComercio(comercioId)).thenReturn(List.of());

        useCase.obtenerMisLotes(comercioId);

        verify(repositorioLote, times(1)).buscarPorComercio(comercioId);
    }

    @Test
    void obtenerMisLotesDeberiaRetornarListaDeLoteResponseConTodosLosEstados() {
        UUID comercioId = UUID.randomUUID();
        LoteExcedente loteDisponible = loteEnEstado(comercioId, EstadoLote.DISPONIBLE, null);
        LoteExcedente loteEntregado = loteEnEstado(comercioId, EstadoLote.ENTREGADO, null);
        when(repositorioLote.buscarPorComercio(comercioId)).thenReturn(List.of(loteDisponible, loteEntregado));

        List<LoteResponse> resultado = useCase.obtenerMisLotes(comercioId);

        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerMisLotesDeberiaRetornarListaVaciaSiElComercioNoTieneLotes() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioLote.buscarPorComercio(comercioId)).thenReturn(List.of());

        List<LoteResponse> resultado = useCase.obtenerMisLotes(comercioId);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerMisReservasDeberiaFiltrarSoloLotesReservadosDelUsuario() {
        UUID usuarioId = UUID.randomUUID();
        LoteExcedente loteDelUsuario = loteEnEstado(UUID.randomUUID(), EstadoLote.RESERVADO, usuarioId);
        LoteExcedente loteDeOtroUsuario = loteEnEstado(UUID.randomUUID(), EstadoLote.RESERVADO, UUID.randomUUID());
        when(repositorioLote.buscarPorEstado(EstadoLote.RESERVADO))
                .thenReturn(List.of(loteDelUsuario, loteDeOtroUsuario));

        List<LoteResponse> resultado = useCase.obtenerMisReservas(usuarioId);

        assertEquals(1, resultado.size());
        assertEquals(loteDelUsuario.getId(), resultado.get(0).id());
    }

    @Test
    void obtenerMisReservasNoDeberiaIncluirLotesReservadosPorOtroUsuario() {
        UUID usuarioId = UUID.randomUUID();
        LoteExcedente loteDeOtroUsuario = loteEnEstado(UUID.randomUUID(), EstadoLote.RESERVADO, UUID.randomUUID());
        when(repositorioLote.buscarPorEstado(EstadoLote.RESERVADO)).thenReturn(List.of(loteDeOtroUsuario));

        List<LoteResponse> resultado = useCase.obtenerMisReservas(usuarioId);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerMisReservasDeberiaRetornarListaVaciaSiNoHayReservasActivas() {
        UUID usuarioId = UUID.randomUUID();
        when(repositorioLote.buscarPorEstado(EstadoLote.RESERVADO)).thenReturn(List.of());

        List<LoteResponse> resultado = useCase.obtenerMisReservas(usuarioId);

        assertTrue(resultado.isEmpty());
    }
}
