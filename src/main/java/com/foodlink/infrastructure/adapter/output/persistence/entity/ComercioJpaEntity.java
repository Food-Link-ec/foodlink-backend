package com.foodlink.infrastructure.adapter.output.persistence.entity;

import com.foodlink.domain.model.comercio.EstadoComercio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comercios")
@Getter
@Setter
@NoArgsConstructor
public class ComercioJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private String nombre;

    private String telefono;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoComercio estado;

    private String provincia;

    private String ciudad;

    @Column(name = "calle_principal")
    private String callePrincipal;

    @Column(name = "calle_secundaria")
    private String calleSecundaria;

    private String referencia;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(nullable = false)
    private boolean activo;
}