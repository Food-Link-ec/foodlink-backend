package com.foodlink.application.usecase.lote;

import com.foodlink.application.dto.request.PublicarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.exception.FechaCaducidadInvalidaException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicarLoteUseCaseTest {

    private static final UUID COMERCIO_ID = UUID.randomUUID();

    @Mock
    private IRepositorioLote repositorioLote;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PublicarLoteUseCaseImpl useCase;

    private void mockGuardarEcheandoElMismoLote() {
        when(repositorioLote.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private PublicarLoteRequest requestVenta() {
        return new PublicarLoteRequest("VENTA", 10.0, new BigDecimal("30"), new BigDecimal("100"),
                LocalDateTime.now().plusDays(3), "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"), null, null, null);
    }

    private PublicarLoteRequest requestDonacion() {
        return new PublicarLoteRequest("DONACION", 10.0, null, null,
                LocalDateTime.now().plusDays(3), "Pan del día", List.of("https://foto.com/1.jpg"), null, null, null);
    }

    private PublicarLoteRequest requestRetiroDirecto() {
        return new PublicarLoteRequest("RETIRO_DIRECTO", 10.0, null, null,
                LocalDateTime.now().plusDays(3), "Verduras variadas", List.of("https://foto.com/1.jpg"), null, null, null);
    }

    @Test
    void publicarConVentaDeberiaRetornarLoteResponseDisponibleConPrecioReducido() {
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.publicar(requestVenta(), COMERCIO_ID);

        assertEquals(EstadoLote.DISPONIBLE.name(), response.estado());
        assertNotNull(response.precioReducido());
    }

    @Test
    void publicarConDonacionDeberiaRetornarLoteResponseConPrecioReducidoNuloYEstadoDisponible() {
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.publicar(requestDonacion(), COMERCIO_ID);

        assertNull(response.precioReducido());
        assertEquals(EstadoLote.DISPONIBLE.name(), response.estado());
    }

    @Test
    void publicarConRetiroDirectoDeberiaRetornarLoteResponseConPrecioReducidoNulo() {
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.publicar(requestRetiroDirecto(), COMERCIO_ID);

        assertNull(response.precioReducido());
    }

    @Test
    void publicarDeberiaLlamarGuardarExactamenteUnaVez() {
        mockGuardarEcheandoElMismoLote();

        useCase.publicar(requestVenta(), COMERCIO_ID);

        verify(repositorioLote, times(1)).guardar(any());
    }

    @Test
    void publicarDeberiaLlamarPublishEventAlMenosUnaVez() {
        mockGuardarEcheandoElMismoLote();

        useCase.publicar(requestVenta(), COMERCIO_ID);

        verify(eventPublisher, atLeastOnce()).publishEvent(any(Object.class));
    }

    @Test
    void publicarConPrecioMayorAlCuarentaPorCientoDelMercadoDeberiaLanzarExcepcion() {
        PublicarLoteRequest request = new PublicarLoteRequest("VENTA", 10.0, new BigDecimal("50"),
                new BigDecimal("100"), LocalDateTime.now().plusDays(3), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"), null, null, null);

        assertThrows(IllegalArgumentException.class, () -> useCase.publicar(request, COMERCIO_ID));
    }

    @Test
    void publicarConCantidadKgCeroDeberiaLanzarIllegalArgumentException() {
        PublicarLoteRequest request = new PublicarLoteRequest("VENTA", 0, new BigDecimal("30"),
                new BigDecimal("100"), LocalDateTime.now().plusDays(3), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"), null, null, null);

        assertThrows(IllegalArgumentException.class, () -> useCase.publicar(request, COMERCIO_ID));
    }

    @Test
    void publicarConCantidadKgNegativaDeberiaLanzarIllegalArgumentException() {
        PublicarLoteRequest request = new PublicarLoteRequest("VENTA", -5, new BigDecimal("30"),
                new BigDecimal("100"), LocalDateTime.now().plusDays(3), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"), null, null, null);

        assertThrows(IllegalArgumentException.class, () -> useCase.publicar(request, COMERCIO_ID));
    }

    @Test
    void publicarConFechaCaducidadEnElPasadoDeberiaLanzarExcepcion() {
        PublicarLoteRequest request = new PublicarLoteRequest("VENTA", 10.0, new BigDecimal("30"),
                new BigDecimal("100"), LocalDateTime.now().minusDays(1), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"), null, null, null);

        assertThrows(FechaCaducidadInvalidaException.class, () -> useCase.publicar(request, COMERCIO_ID));
    }

    @Test
    void loteResponseDeberiaTenerPrecioNormalIgualAlPrecioMercadoDelRequest() {
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.publicar(requestVenta(), COMERCIO_ID);

        assertEquals(0, new BigDecimal("100").compareTo(response.precioNormal()));
    }

    @Test
    void loteResponseDeberiaTenerComercioIdIgualAlUuidDelComercioAutenticado() {
        mockGuardarEcheandoElMismoLote();

        LoteResponse response = useCase.publicar(requestVenta(), COMERCIO_ID);

        assertEquals(COMERCIO_ID, response.comercioId());
    }
}