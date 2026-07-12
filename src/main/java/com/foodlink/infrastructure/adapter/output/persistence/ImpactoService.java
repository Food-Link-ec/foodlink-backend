package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ImpactoDashboardResponse;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ImpactoMetricaJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ImpactoService {

    private final ImpactoMetricaJpaRepository jpaRepository;

    public ImpactoService(ImpactoMetricaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public void registrar(UUID loteId, double cantidadKg) {
        double co2 = cantidadKg * 2.5;
        int personas = (int) Math.ceil(cantidadKg / 0.5);

        ImpactoMetricaJpaEntity entity = new ImpactoMetricaJpaEntity();
        entity.setLoteId(loteId);
        entity.setCantidadKg(cantidadKg);
        entity.setCo2EvitadoKg(co2);
        entity.setPersonasBeneficiadas(personas);
        entity.setFechaEntrega(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    public ImpactoDashboardResponse consultarDashboard() {
        double kg = jpaRepository.sumCantidadKg() != null
                ? jpaRepository.sumCantidadKg() : 0.0;
        double co2 = jpaRepository.sumCo2EvitadoKg() != null
                ? jpaRepository.sumCo2EvitadoKg() : 0.0;
        int personas = jpaRepository.sumPersonasBeneficiadas() != null
                ? jpaRepository.sumPersonasBeneficiadas() : 0;
        long lotes = jpaRepository.countLotesEntregados() != null
                ? jpaRepository.countLotesEntregados() : 0L;

        return new ImpactoDashboardResponse(
                kg, co2, personas, lotes,
                "FoodLink ha rescatado " + String.format("%.1f", kg) +
                        " kg de alimentos beneficiando a " + personas + " personas."
        );
    }
}