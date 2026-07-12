package com.foodlink.infrastructure.adapter.input.event;

import com.foodlink.domain.event.EntregaConfirmada;
import com.foodlink.infrastructure.adapter.output.persistence.ImpactoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImpactoEventListenerTest {

    @Mock
    private ImpactoService impactoService;

    @InjectMocks
    private ImpactoEventListener listener;

    @Test
    void onEntregaConfirmadaDeberiaLlamarRegistrarConLoteIdYCantidadKgCorrectos() {
        UUID loteId = UUID.randomUUID();
        EntregaConfirmada evento = new EntregaConfirmada(loteId, UUID.randomUUID(), UUID.randomUUID(), 8.0, LocalDateTime.now());

        listener.onEntregaConfirmada(evento);

        verify(impactoService, times(1)).registrar(loteId, 8.0);
    }
}