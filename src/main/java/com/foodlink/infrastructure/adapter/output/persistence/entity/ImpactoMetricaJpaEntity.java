package com.foodlink.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "impacto_metricas")
@Getter
@Setter
@NoArgsConstructor
public class ImpactoMetricaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private UUID loteId;

    @Column(name = "cantidad_kg", nullable = false, columnDefinition = "numeric")
    private double cantidadKg;

    @Column(name = "co2_evitado_kg", nullable = false, columnDefinition = "numeric")
    private double co2EvitadoKg;

    @Column(name = "personas_beneficiadas", nullable = false)
    private int personasBeneficiadas;

    @Column(name = "fecha_entrega", nullable = false)
    private LocalDateTime fechaEntrega;
}