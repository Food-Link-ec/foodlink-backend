package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.ValoracionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ValoracionJpaRepository extends JpaRepository<ValoracionJpaEntity, Long> {

    List<ValoracionJpaEntity> findByComercioId(UUID comercioId);

    List<ValoracionJpaEntity> findByLoteId(UUID loteId);

    @Query("SELECT AVG(v.puntuacion) FROM ValoracionJpaEntity v WHERE v.comercioId = :comercioId")
    Double promedioByComercio(@Param("comercioId") UUID comercioId);

    @Query("SELECT COUNT(v) FROM ValoracionJpaEntity v WHERE v.comercioId = :comercioId")
    Long countByComercio(@Param("comercioId") UUID comercioId);
}
