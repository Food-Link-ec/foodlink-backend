package com.foodlink.infrastructure.adapter.output.persistence.repository;

import com.foodlink.infrastructure.adapter.output.persistence.entity.LoteJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<LoteJpaEntity> findByEstado(String estado, Pageable pageable);

    Page<LoteJpaEntity> findByComercioId(UUID comercioId, Pageable pageable);

    Page<LoteJpaEntity> findByEstadoAndModalidad(String estado, String modalidad, Pageable pageable);

    @Query("SELECT l FROM LoteJpaEntity l WHERE " +
           "LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "AND l.estado = 'DISPONIBLE'")
    Page<LoteJpaEntity> buscarPorTexto(@Param("texto") String texto, Pageable pageable);

    @Query("SELECT l FROM LoteJpaEntity l WHERE " +
           "LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "AND l.estado = 'DISPONIBLE' AND l.modalidad = :modalidad")
    Page<LoteJpaEntity> buscarPorTextoYModalidad(@Param("texto") String texto,
                                                  @Param("modalidad") String modalidad,
                                                  Pageable pageable);

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.estado = 'RESERVADO' AND l.inicioReserva < :limite")
    List<LoteJpaEntity> findReservasExpiradas(@Param("limite") LocalDateTime limite);

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.estado IN ('DISPONIBLE', 'RESERVADO') AND l.fechaCaducidad < :ahora")
    List<LoteJpaEntity> findLotesCaducados(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT COUNT(l) FROM LoteJpaEntity l WHERE l.estado = :estado")
    Long countByEstado(@Param("estado") String estado);

    @Query(value = "SELECT l.comercio_id, COUNT(i.id) as lotes, SUM(i.cantidad_kg) as kg " +
           "FROM impacto_metricas i " +
           "JOIN lotes_excedentes l ON i.lote_id = l.id " +
           "GROUP BY l.comercio_id " +
           "ORDER BY lotes DESC LIMIT 5",
           nativeQuery = true)
    List<Object[]> findTop5ComerciosPorImpacto();

    @Query(value = "SELECT COUNT(l.id) as lotes_comprados, " +
           "COALESCE(SUM(l.precio_monto), 0) as total_pagado, " +
           "COALESCE(SUM(l.cantidad_kg), 0) as kg_comprados " +
           "FROM lotes_excedentes l " +
           "WHERE l.beneficiario_reserva_id = :usuarioId " +
           "AND l.modalidad = 'VENTA' " +
           "AND l.estado IN ('VENDIDO', 'ENTREGADO')",
           nativeQuery = true)
    Object[] estadisticasCompras(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT l FROM LoteJpaEntity l WHERE " +
           "l.estado = 'DISPONIBLE' AND l.categoria = :categoria")
    Page<LoteJpaEntity> findByCategoria(@Param("categoria") String categoria, Pageable pageable);

    @Query("SELECT l FROM LoteJpaEntity l WHERE " +
           "l.estado = 'DISPONIBLE' AND l.categoria = :categoria " +
           "AND l.modalidad = :modalidad")
    Page<LoteJpaEntity> findByCategoriaAndModalidad(@Param("categoria") String categoria,
                                                      @Param("modalidad") String modalidad,
                                                      Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(l.precio_monto), 0) FROM lotes_excedentes l " +
           "WHERE l.beneficiario_reserva_id = :usuarioId " +
           "AND l.modalidad = 'VENTA' " +
           "AND l.estado IN ('VENDIDO', 'ENTREGADO') " +
           "AND DATE_TRUNC('month', l.actualizado_en) = DATE_TRUNC('month', CURRENT_DATE - CAST(:mesesAtras || ' months' AS interval))",
           nativeQuery = true)
    Double sumPagadoMes(@Param("usuarioId") UUID usuarioId, @Param("mesesAtras") int mesesAtras);

    @Query(value = "SELECT COALESCE(SUM(l.precio_monto), 0) FROM lotes_excedentes l " +
           "WHERE l.comercio_id = :comercioId " +
           "AND l.modalidad = 'VENTA' " +
           "AND l.estado IN ('VENDIDO', 'ENTREGADO') " +
           "AND DATE_TRUNC('month', l.actualizado_en) = DATE_TRUNC('month', CURRENT_DATE)",
           nativeQuery = true)
    Double sumIngresosMesActualByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT COALESCE(SUM(l.precio_monto), 0) FROM lotes_excedentes l " +
           "WHERE l.comercio_id = :comercioId " +
           "AND l.modalidad = 'VENTA' " +
           "AND l.estado IN ('VENDIDO', 'ENTREGADO') " +
           "AND DATE_TRUNC('month', l.actualizado_en) = DATE_TRUNC('month', CURRENT_DATE - CAST('1 month' AS interval))",
           nativeQuery = true)
    Double sumIngresosMesAnteriorByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT COUNT(l.id) FROM lotes_excedentes l " +
           "WHERE l.comercio_id = :comercioId AND l.estado = 'RESERVADO'",
           nativeQuery = true)
    Long countReservasPendientesByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT l.id, l.descripcion, l.cantidad_kg, l.actualizado_en, l.estado, " +
           "(SELECT lf.url FROM lote_fotos lf WHERE lf.lote_id = l.id LIMIT 1) as foto_url " +
           "FROM lotes_excedentes l " +
           "WHERE l.comercio_id = :comercioId " +
           "ORDER BY l.actualizado_en DESC LIMIT 4",
           nativeQuery = true)
    List<Object[]> findLotesRecientesByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT l.id, l.descripcion, l.cantidad_kg, l.fecha_caducidad, " +
           "COALESCE(b.nombre, 'Organización') as org_nombre " +
           "FROM lotes_excedentes l " +
           "LEFT JOIN beneficiarios b ON b.id = l.beneficiario_reserva_id " +
           "WHERE l.comercio_id = :comercioId " +
           "AND l.modalidad = 'DONACION' " +
           "AND l.estado = 'RESERVADO' " +
           "ORDER BY l.fecha_caducidad ASC LIMIT 3",
           nativeQuery = true)
    List<Object[]> findReservasUrgentesDonacionByComercio(@Param("comercioId") UUID comercioId);

    @Query(value = "SELECT COUNT(l.id) FROM lotes_excedentes l " +
           "WHERE l.comercio_id = :comercioId " +
           "AND l.estado IN ('DISPONIBLE', 'RESERVADO') " +
           "AND l.fecha_caducidad > NOW()",
           nativeQuery = true)
    Long countLotesActivosByComercio(@Param("comercioId") UUID comercioId);

    List<LoteJpaEntity> findByBeneficiarioReservaId(UUID beneficiarioReservaId);
}
