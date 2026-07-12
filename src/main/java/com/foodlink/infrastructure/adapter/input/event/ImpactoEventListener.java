package com.foodlink.infrastructure.adapter.input.event;

import com.foodlink.domain.event.EntregaConfirmada;
import com.foodlink.infrastructure.adapter.output.persistence.ImpactoService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ImpactoEventListener {

    private final ImpactoService impactoService;

    public ImpactoEventListener(ImpactoService impactoService) {
        this.impactoService = impactoService;
    }

    @EventListener
    public void onEntregaConfirmada(EntregaConfirmada evento) {
        impactoService.registrar(evento.loteId(), evento.cantidadKg());
    }
}