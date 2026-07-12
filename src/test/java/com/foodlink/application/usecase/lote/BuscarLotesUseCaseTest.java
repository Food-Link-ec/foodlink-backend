package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.BuscarLotesRequest;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarLotesUseCaseTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();

    @Mock
    private IRepositorioLote repositorioLote;

    @InjectMocks
    private BuscarLotesUseCaseImpl useCase;

    private FechaCaducidad fechaCaducidadValida() {
        return FechaCaducidad.de(LocalDateTime.now().plusDays(3));
    }

    private LoteExcedente loteVenta() {
        return LoteExcedente.reconstituir(UUID.randomUUID(), COMERCIO_ID, Modalidad.VENTA, EstadoLote.DISPONIBLE,
                10, Dinero.de(new BigDecimal("30"), "USD"), fechaCaducidadValida(), LocalDateTime.now(),
                "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"), null, null, null, null);
    }

    private LoteExcedente loteDonacion() {
        return LoteExcedente.reconstituir(UUID.randomUUID(), COMERCIO_ID, Modalidad.DONACION, EstadoLote.DISPONIBLE,
                10, Dinero.cero(), fechaCaducidadValida(), LocalDateTime.now(),
                "Pan del día", List.of("https://foto.com/1.jpg"), null, null, null, null);
    }

    @Test
    void buscarSinFiltrosDeberiaLlamarBuscarDisponiblesYRetornarLista() {
        when(repositorioLote.buscarDisponibles()).thenReturn(List.of(loteVenta()));

        List<LoteResponse> resultado = useCase.buscar(new BuscarLotesRequest(null, null, null, null, null, null, null, 0, 10));

        verify(repositorioLote, times(1)).buscarDisponibles();
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarConModalidadDonacionDeberiaLlamarBuscarDisponiblesPorModalidadDonacion() {
        when(repositorioLote.buscarDisponiblesPorModalidad(Modalidad.DONACION)).thenReturn(List.of(loteDonacion()));

        useCase.buscar(new BuscarLotesRequest("DONACION", null, null, null, null, null, null, 0, 10));

        verify(repositorioLote, times(1)).buscarDisponiblesPorModalidad(Modalidad.DONACION);
    }

    @Test
    void buscarConModalidadVentaDeberiaLlamarBuscarDisponiblesPorModalidadVenta() {
        when(repositorioLote.buscarDisponiblesPorModalidad(Modalidad.VENTA)).thenReturn(List.of(loteVenta()));

        useCase.buscar(new BuscarLotesRequest("VENTA", null, null, null, null, null, null, 0, 10));

        verify(repositorioLote, times(1)).buscarDisponiblesPorModalidad(Modalidad.VENTA);
    }

    @Test
    void buscarConComercioIdDeberiaLlamarBuscarPorComercio() {
        when(repositorioLote.buscarPorComercio(COMERCIO_ID)).thenReturn(List.of(loteVenta()));

        useCase.buscar(new BuscarLotesRequest(null, null, COMERCIO_ID, null, null, null, null, 0, 10));

        verify(repositorioLote, times(1)).buscarPorComercio(COMERCIO_ID);
    }

    @Test
    void buscarConEstadoExpiradoDeberiaLlamarBuscarPorEstadoExpirado() {
        when(repositorioLote.buscarPorEstado(EstadoLote.EXPIRADO)).thenReturn(List.of());

        useCase.buscar(new BuscarLotesRequest(null, "EXPIRADO", null, null, null, null, null, 0, 10));

        verify(repositorioLote, times(1)).buscarPorEstado(EstadoLote.EXPIRADO);
    }

    @Test
    void buscarPorIdConIdExistenteDeberiaRetornarLoteResponseCorrecto() {
        LoteExcedente lote = loteVenta();
        when(repositorioLote.buscarPorId(lote.getId())).thenReturn(Optional.of(lote));

        LoteResponse response = useCase.buscarPorId(lote.getId());

        assertEquals(lote.getId(), response.id());
        assertEquals(lote.getDescripcion(), response.descripcion());
    }

    @Test
    void buscarPorIdConIdInexistenteDeberiaLanzarIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        when(repositorioLote.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.buscarPorId(id));
    }

    @Test
    void loteResponseDeVentaDeberiaTenerPrecioReducidoNoNulo() {
        LoteExcedente lote = loteVenta();
        when(repositorioLote.buscarPorId(lote.getId())).thenReturn(Optional.of(lote));

        LoteResponse response = useCase.buscarPorId(lote.getId());

        assertNotNull(response.precioReducido());
    }

    @Test
    void loteResponseDeDonacionDeberiaTenerPrecioReducidoNulo() {
        LoteExcedente lote = loteDonacion();
        when(repositorioLote.buscarPorId(lote.getId())).thenReturn(Optional.of(lote));

        LoteResponse response = useCase.buscarPorId(lote.getId());

        assertNull(response.precioReducido());
    }
}