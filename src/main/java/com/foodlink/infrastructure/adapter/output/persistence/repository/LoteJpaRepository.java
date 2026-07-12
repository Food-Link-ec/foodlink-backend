package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoteJpaRepository extends JpaRepository<LoteJpaEntity, UUID> {

    List<LoteJpaEntity> findByEstado(String estado);

    List<LoteJpaEntity> findByComercioId(UUID comercioId);

    List<LoteJpaEntity> findByEstadoAndModalidad(String estado, String modalidad);

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.estado = 'RESERVADO' AND l.inicioReserva < :limite")
    List<LoteJpaEntity> findReservasExpiradas(@Param("limite") LocalDateTime limite);

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.estado IN ('DISPONIBLE', 'RESERVADO') AND l.fechaCaducidad < :ahora")
    List<LoteJpaEntity> findLotesCaducados(@Param("ahora") LocalDateTime ahora);
}
