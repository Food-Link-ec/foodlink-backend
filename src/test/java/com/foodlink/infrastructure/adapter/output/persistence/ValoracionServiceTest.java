package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ResumenValoracionesResponse;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ValoracionJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ValoracionJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValoracionServiceTest {

    @Mock
    private ValoracionJpaRepository jpaRepository;

    @Mock
    private IRepositorioComercio repositorioComercio;

    private ValoracionService valoracionService;

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", -0.180653, -78.467834);
    }

    private Comercio comercioValido() {
        return Comercio.crear("1792146739001", "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
    }

    private ValoracionJpaEntity valoracion(UUID comercioId, int puntuacion) {
        ValoracionJpaEntity entity = new ValoracionJpaEntity();
        entity.setId(1L);
        entity.setLoteId(UUID.randomUUID());
        entity.setComercioId(comercioId);
        entity.setPuntuacion(puntuacion);
        entity.setComentario("Buen servicio");
        entity.setCreadoEn(LocalDateTime.now());
        return entity;
    }

    private void construirServicio() {
        valoracionService = new ValoracionService(jpaRepository, repositorioComercio);
    }

    @Test
    void obtenerValoracionesDeberiaRetornarComercioIdCorrecto() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.promedioByComercio(comercioId)).thenReturn(4.5);
        when(jpaRepository.countByComercio(comercioId)).thenReturn(2L);
        when(jpaRepository.findByComercioId(comercioId)).thenReturn(List.of(valoracion(comercioId, 5)));
        construirServicio();

        ResumenValoracionesResponse response = valoracionService.obtenerValoraciones(comercioId);

        assertEquals(comercioId, response.comercioId());
    }

    @Test
    void obtenerValoracionesDeberiaCalcularPromedioCorrectamente() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.promedioByComercio(comercioId)).thenReturn(4.333333);
        when(jpaRepository.countByComercio(comercioId)).thenReturn(3L);
        when(jpaRepository.findByComercioId(comercioId)).thenReturn(List.of());
        construirServicio();

        ResumenValoracionesResponse response = valoracionService.obtenerValoraciones(comercioId);

        assertEquals(4.3, response.promedioEstrellas());
    }

    @Test
    void obtenerValoracionesConCeroValoracionesDeberiaRetornarPromedioCero() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.promedioByComercio(comercioId)).thenReturn(null);
        when(jpaRepository.countByComercio(comercioId)).thenReturn(null);
        when(jpaRepository.findByComercioId(comercioId)).thenReturn(List.of());
        construirServicio();

        ResumenValoracionesResponse response = valoracionService.obtenerValoraciones(comercioId);

        assertEquals(0.0, response.promedioEstrellas());
        assertEquals(0L, response.totalValoraciones());
    }

    @Test
    void obtenerMisValoracionesDeberiaDelegarAObtenerValoraciones() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.promedioByComercio(comercioId)).thenReturn(5.0);
        when(jpaRepository.countByComercio(comercioId)).thenReturn(1L);
        when(jpaRepository.findByComercioId(comercioId)).thenReturn(List.of(valoracion(comercioId, 5)));
        construirServicio();

        ResumenValoracionesResponse response = valoracionService.obtenerMisValoraciones(comercioId);

        assertEquals(comercioId, response.comercioId());
        assertEquals(5.0, response.promedioEstrellas());
    }
}
