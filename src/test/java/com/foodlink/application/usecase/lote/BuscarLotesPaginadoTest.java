package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.application.dto.response.PageResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.domain.port.output.IRepositorioLote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarLotesPaginadoTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();

    @Mock
    private IRepositorioLote repositorioLote;

    @InjectMocks
    private BuscarLotesUseCaseImpl useCase;

    private LoteExcedente loteVenta() {
        return LoteExcedente.reconstituir(UUID.randomUUID(), COMERCIO_ID, Modalidad.VENTA, EstadoLote.DISPONIBLE,
                10, Dinero.de(new BigDecimal("30"), "USD"), FechaCaducidad.de(LocalDateTime.now().plusDays(3)),
                LocalDateTime.now(), "Pan integral del dia", List.of("https://foto.com/1.jpg"),
                null, null, null, null, null);
    }

    private BuscarLotesRequest requestSinFiltros() {
        return new BuscarLotesRequest(null, null, null, null, null, null, null, null, 0, 10);
    }

    @Test
    void buscarPaginadoSinFiltrosDeberiaLlamarBuscarPaginadoEnRepositorio() {
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        useCase.buscarPaginado(requestSinFiltros());

        verify(repositorioLote, times(1)).buscarPaginado(any());
    }

    @Test
    void buscarPaginadoConTextoDeberiaDelegarEnRepositorioConElMismoRequest() {
        BuscarLotesRequest request = new BuscarLotesRequest(null, null, null, null, null, null, "pan", null, 0, 10);
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        useCase.buscarPaginado(request);

        ArgumentCaptor<BuscarLotesRequest> captor = ArgumentCaptor.forClass(BuscarLotesRequest.class);
        verify(repositorioLote).buscarPaginado(captor.capture());
        assertEquals("pan", captor.getValue().q());
    }

    @Test
    void buscarPaginadoConLatLngRadioDeberiaUsarBuscarDisponiblesCercanosConPaginacionManual() {
        BuscarLotesRequest request = new BuscarLotesRequest(
                null, null, null, -0.18, -78.46, 5.0, null, null, 0, 10);
        when(repositorioLote.buscarDisponiblesCercanos(-0.18, -78.46, 5.0))
                .thenReturn(List.of(loteVenta()));

        useCase.buscarPaginado(request);

        verify(repositorioLote, times(1)).buscarDisponiblesCercanos(-0.18, -78.46, 5.0);
        verify(repositorioLote, never()).buscarPaginado(any());
    }

    @Test
    void buscarPaginadoDeberiaRetornarPageResponseConPaginaActualYTotalElementos() {
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 1), 3);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        PageResponse<LoteResponse> resultado = useCase.buscarPaginado(requestSinFiltros());

        assertEquals(0, resultado.paginaActual());
        assertEquals(3, resultado.totalElementos());
    }

    @Test
    void buscarPaginadoConPageCeroSizeDiezDeberiaRespetarPaginacion() {
        BuscarLotesRequest request = new BuscarLotesRequest(null, null, null, null, null, null, null, null, 0, 10);
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        PageResponse<LoteResponse> resultado = useCase.buscarPaginado(request);

        assertEquals(10, resultado.tamanioPagina());
        assertEquals(1, resultado.contenido().size());
    }

    @Test
    void buscarPaginadoConCategoriaDeberiaDelegarCategoriaAlRepositorio() {
        BuscarLotesRequest request = new BuscarLotesRequest(null, null, null, null, null, null, null, "PANADERIA", 0, 10);
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        useCase.buscarPaginado(request);

        ArgumentCaptor<BuscarLotesRequest> captor = ArgumentCaptor.forClass(BuscarLotesRequest.class);
        verify(repositorioLote).buscarPaginado(captor.capture());
        assertEquals("PANADERIA", captor.getValue().categoria());
    }

    @Test
    void buscarPaginadoConCategoriaYModalidadDeberiaDelegarAmbosAlRepositorio() {
        BuscarLotesRequest request = new BuscarLotesRequest("VENTA", null, null, null, null, null, null, "PANADERIA", 0, 10);
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        useCase.buscarPaginado(request);

        ArgumentCaptor<BuscarLotesRequest> captor = ArgumentCaptor.forClass(BuscarLotesRequest.class);
        verify(repositorioLote).buscarPaginado(captor.capture());
        assertEquals("PANADERIA", captor.getValue().categoria());
        assertEquals("VENTA", captor.getValue().modalidad());
    }

    @Test
    void buscarPaginadoConCategoriaSinModalidadDeberiaFiltrarSoloPorCategoria() {
        BuscarLotesRequest request = new BuscarLotesRequest(null, null, null, null, null, null, null, "LACTEOS", 0, 10);
        Page<LoteExcedente> page = new PageImpl<>(List.of(loteVenta()), PageRequest.of(0, 10), 1);
        when(repositorioLote.buscarPaginado(any())).thenReturn(page);

        useCase.buscarPaginado(request);

        ArgumentCaptor<BuscarLotesRequest> captor = ArgumentCaptor.forClass(BuscarLotesRequest.class);
        verify(repositorioLote).buscarPaginado(captor.capture());
        assertEquals("LACTEOS", captor.getValue().categoria());
        assertEquals(null, captor.getValue().modalidad());
    }

    @Test
    void buscarHistorialExpiradosDeberiaLlamarBuscarPorComercioYEstadoExpirado() {
        when(repositorioLote.buscarPorComercioYEstado(COMERCIO_ID, EstadoLote.EXPIRADO)).thenReturn(List.of());

        useCase.buscarHistorialExpirados(COMERCIO_ID);

        verify(repositorioLote, times(1)).buscarPorComercioYEstado(COMERCIO_ID, EstadoLote.EXPIRADO);
    }

    @Test
    void buscarHistorialExpiradosDeberiaRetornarListaDeLoteResponse() {
        LoteExcedente lote = loteVenta();
        when(repositorioLote.buscarPorComercioYEstado(COMERCIO_ID, EstadoLote.EXPIRADO))
                .thenReturn(List.of(lote));

        List<LoteResponse> resultado = useCase.buscarHistorialExpirados(COMERCIO_ID);

        assertEquals(1, resultado.size());
        assertEquals(lote.getId(), resultado.get(0).id());
    }
}
