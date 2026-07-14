package com.foodlink.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lotes_excedentes")
@Getter
@Setter
@NoArgsConstructor
public class LoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "comercio_id", nullable = false)
    private UUID comercioId;

    @Column(nullable = false, length = 20)
    private String modalidad;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "cantidad_kg", nullable = false, columnDefinition = "numeric")
    private double cantidadKg;

    @Column(name = "precio_monto")
    private BigDecimal precioMonto;

    @Column(name = "precio_original", columnDefinition = "numeric")
    private BigDecimal precioOriginal;

    @Column(name = "precio_moneda", length = 3)
    private String precioMoneda;

    @Column(name = "fecha_caducidad", nullable = false)
    private LocalDateTime fechaCaducidad;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "beneficiario_reserva_id")
    private UUID beneficiarioReservaId;

    @Column(name = "inicio_reserva")
    private LocalDateTime inicioReserva;

    @Column(columnDefinition = "numeric")
    private Double latitud;

    @Column(columnDefinition = "numeric")
    private Double longitud;

    @Column(length = 20)
    private String categoria;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @OneToMany(
            mappedBy = "lote",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<LoteFotoJpaEntity> fotos = new ArrayList<>();
}