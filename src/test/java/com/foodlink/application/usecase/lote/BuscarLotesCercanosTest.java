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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarLotesCercanosTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();
    private static final double LATITUD = -0.1807;
    private static final double LONGITUD = -78.4897;
    private static final double RADIO_KM = 5.0;

    @Mock
    private IRepositorioLote repositorioLote;

    @InjectMocks
    private BuscarLotesUseCaseImpl useCase;

    private LoteExcedente loteConUbicacion(Double latitud, Double longitud) {
        LoteExcedente lote = LoteExcedente.reconstituir(UUID.randomUUID(), COMERCIO_ID, Modalidad.VENTA,
                EstadoLote.DISPONIBLE, 10, Dinero.de(new BigDecimal("30"), "USD"),
                FechaCaducidad.de(LocalDateTime.now().plusDays(3)), LocalDateTime.now(),
                "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"), null, null, null, null);
        lote.asignarUbicacion(latitud, longitud);
        return lote;
    }

    @Test
    void buscarConLatLngRadioDeberiaLlamarBuscarDisponiblesCercanos() {
        when(repositorioLote.buscarDisponiblesCercanos(LATITUD, LONGITUD, RADIO_KM)).thenReturn(List.of());

        useCase.buscar(new BuscarLotesRequest(null, null, null, LATITUD, LONGITUD, RADIO_KM));

        verify(repositorioLote, times(1)).buscarDisponiblesCercanos(LATITUD, LONGITUD, RADIO_KM);
        verify(repositorioLote, never()).buscarDisponibles();
        verify(repositorioLote, never()).buscarPorComercio(any());
    }

    @Test
    void buscarConLatLngRadioDeberiaRetornarSoloLotesConUbicacion() {
        LoteExcedente loteCercano = loteConUbicacion(-0.1810, -78.4900);
        when(repositorioLote.buscarDisponiblesCercanos(LATITUD, LONGITUD, RADIO_KM))
                .thenReturn(List.of(loteCercano));

        List<LoteResponse> resultado = useCase.buscar(
                new BuscarLotesRequest(null, null, null, LATITUD, LONGITUD, RADIO_KM));

        assertEquals(1, resultado.size());
        assertEquals(loteCercano.getId(), resultado.get(0).id());
    }

    @Test
    void buscarSinLatLngNoDeberiaLlamarBuscarDisponiblesCercanos() {
        when(repositorioLote.buscarDisponibles()).thenReturn(List.of());

        useCase.buscar(new BuscarLotesRequest(null, null, null, null, null, null));

        verify(repositorioLote, never()).buscarDisponiblesCercanos(anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void loteResponseDeberiaIncluirLatitudYLongitud() {
        LoteExcedente lote = loteConUbicacion(LATITUD, LONGITUD);
        when(repositorioLote.buscarDisponiblesCercanos(LATITUD, LONGITUD, RADIO_KM)).thenReturn(List.of(lote));

        List<LoteResponse> resultado = useCase.buscar(
                new BuscarLotesRequest(null, null, null, LATITUD, LONGITUD, RADIO_KM));

        assertEquals(LATITUD, resultado.get(0).latitud());
        assertEquals(LONGITUD, resultado.get(0).longitud());
    }
}
