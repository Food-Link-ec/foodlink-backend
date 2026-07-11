package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoteJpaRepository extends JpaRepository<LoteJpaEntity, UUID> {

    List<LoteJpaEntity> findByEstado(String estado);

    List<LoteJpaEntity> findByComercioId(UUID comercioId);

    List<LoteJpaEntity> findByEstadoAndModalidad(String estado, String modalidad);
}