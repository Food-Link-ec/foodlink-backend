package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.ImpactoMetricaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImpactoMetricaJpaRepository extends JpaRepository<ImpactoMetricaJpaEntity, Long> {

    List<ImpactoMetricaJpaEntity> findAllByOrderByFechaEntregaDesc();

    @Query("SELECT SUM(i.cantidadKg) FROM ImpactoMetricaJpaEntity i")
    Double sumCantidadKg();

    @Query("SELECT SUM(i.co2EvitadoKg) FROM ImpactoMetricaJpaEntity i")
    Double sumCo2EvitadoKg();

    @Query("SELECT SUM(i.personasBeneficiadas) FROM ImpactoMetricaJpaEntity i")
    Integer sumPersonasBeneficiadas();

    @Query("SELECT COUNT(i) FROM ImpactoMetricaJpaEntity i")
    Long countLotesEntregados();
}