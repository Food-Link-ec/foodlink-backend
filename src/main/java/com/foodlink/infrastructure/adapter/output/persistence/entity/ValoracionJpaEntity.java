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
@Table(name = "valoraciones")
@Getter
@Setter
@NoArgsConstructor
public class ValoracionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false)
    private UUID loteId;

    @Column(name = "comercio_id", nullable = false)
    private UUID comercioId;

    @Column(nullable = false)
    private int puntuacion;

    @Column(length = 300)
    private String comentario;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;
}
