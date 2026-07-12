package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.ImpactoMetricaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

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

    @Query("SELECT SUM(i.cantidadKg) FROM ImpactoMetricaJpaEntity i " +
           "WHERE i.loteId IN (SELECT l.id FROM LoteJpaEntity l WHERE l.comercioId = :comercioId)")
    Double sumCantidadKgByComercio(@Param("comercioId") UUID comercioId);

    @Query("SELECT SUM(i.co2EvitadoKg) FROM ImpactoMetricaJpaEntity i " +
           "WHERE i.loteId IN (SELECT l.id FROM LoteJpaEntity l WHERE l.comercioId = :comercioId)")
    Double sumCo2ByComercio(@Param("comercioId") UUID comercioId);

    @Query("SELECT SUM(i.personasBeneficiadas) FROM ImpactoMetricaJpaEntity i " +
           "WHERE i.loteId IN (SELECT l.id FROM LoteJpaEntity l WHERE l.comercioId = :comercioId)")
    Integer sumPersonasByComercio(@Param("comercioId") UUID comercioId);

    @Query("SELECT COUNT(i) FROM ImpactoMetricaJpaEntity i " +
           "WHERE i.loteId IN (SELECT l.id FROM LoteJpaEntity l WHERE l.comercioId = :comercioId)")
    Long countLotesByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT SUM(i.cantidad_kg) FROM impacto_metricas i " +
           "JOIN lotes_excedentes l ON i.lote_id = l.id " +
           "WHERE l.comercio_id = :comercioId " +
           "AND DATE_TRUNC('month', i.fecha_entrega) = DATE_TRUNC('month', CURRENT_DATE)",
           nativeQuery = true)
    Double sumCantidadKgMesActualByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT SUM(cantidad_kg) FROM impacto_metricas " +
           "WHERE DATE_TRUNC('month', fecha_entrega) = DATE_TRUNC('month', CURRENT_DATE)",
           nativeQuery = true)
    Double sumCantidadKgMesActual();
}