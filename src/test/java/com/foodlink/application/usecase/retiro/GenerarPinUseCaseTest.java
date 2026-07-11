package com.foodlink.application.usecase.retiro;

import com.foodlink.application.dto.response.PinRetiroResponse;
import com.foodlink.domain.model.shared.PinRetiro;
import com.foodlink.domain.port.output.IRepositorioPin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerarPinUseCaseTest {

    @Mock
    private IRepositorioPin repositorioPin;

    @InjectMocks
    private GenerarPinUseCaseImpl useCase;

    @Test
    void generarPinSinPinPrevioDeberiaGuardarYRetornarResponse() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(false);

        PinRetiroResponse response = useCase.generarPin(loteId);

        verify(repositorioPin, never()).eliminarPorLote(any());
        verify(repositorioPin, times(1)).guardar(eq(loteId), any(PinRetiro.class), any(LocalDateTime.class));
        assertEquals(loteId, response.loteId());
    }

    @Test
    void generarPinConPinPrevioDeberiaEliminarAntesDeGuardar() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(true);

        useCase.generarPin(loteId);

        verify(repositorioPin, times(1)).eliminarPorLote(loteId);
        verify(repositorioPin, times(1)).guardar(eq(loteId), any(PinRetiro.class), any(LocalDateTime.class));
    }

    @Test
    void pinRetornadoDeberiaTenerExactamenteCincoCaracteres() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(false);

        PinRetiroResponse response = useCase.generarPin(loteId);

        assertEquals(5, response.pin().length());
    }

    @Test
    void qrDataRetornadoDeberiaContenerPrefijoEsperado() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(false);

        PinRetiroResponse response = useCase.generarPin(loteId);

        assertTrue(response.qrData().contains("FOODLINK:RETIRO:"));
    }

    @Test
    void expiraEnDeberiaSerAproximadamenteVeinticuatroHorasDesdeAhora() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(false);

        LocalDateTime antes = LocalDateTime.now().plusHours(24);
        PinRetiroResponse response = useCase.generarPin(loteId);
        LocalDateTime despues = LocalDateTime.now().plusHours(24);

        assertTrue(Duration.between(antes, response.expiraEn()).abs().toMinutes() <= 1);
        assertTrue(Duration.between(response.expiraEn(), despues).abs().toMinutes() <= 1);
    }

    @Test
    void mensajeRetornadoDeberiaSerElEsperado() {
        UUID loteId = UUID.randomUUID();
        when(repositorioPin.existePorLote(loteId)).thenReturn(false);

        PinRetiroResponse response = useCase.generarPin(loteId);

        assertEquals("PIN generado. Válido por 24 horas.", response.mensaje());
    }
}