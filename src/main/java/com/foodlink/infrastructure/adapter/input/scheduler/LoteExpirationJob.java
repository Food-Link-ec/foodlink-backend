package com.foodlink.infrastructure.adapter.input.scheduler;

import com.foodlink.domain.port.output.IRepositorioLote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoteExpirationJob {

    private final IRepositorioLote repositorioLote;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${foodlink.reserva.timeout-minutos:30}")
    private int timeoutMinutos;

    public LoteExpirationJob(
            IRepositorioLote repositorioLote,
            ApplicationEventPublisher eventPublisher) {
        this.repositorioLote = repositorioLote;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "${foodlink.scheduler.reservas-delay-ms:60000}")
    public void cancelarReservasExpiradas() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(timeoutMinutos);

        repositorioLote.buscarReservasExpiradas(limite)
                .forEach(lote -> {
                    lote.cancelarReserva("Reserva expirada automaticamente por timeout de " + timeoutMinutos + " minutos");
                    repositorioLote.guardar(lote);
                    lote.pullEventos().forEach(eventPublisher::publishEvent);
                });
    }

    @Scheduled(fixedDelayString = "${foodlink.scheduler.lotes-delay-ms:300000}")
    public void expirarLotesCaducados() {
        repositorioLote.buscarLotesCaducados()
                .forEach(lote -> {
                    lote.expirar();
                    repositorioLote.guardar(lote);
                    lote.pullEventos().forEach(eventPublisher::publishEvent);
                });
    }
}
