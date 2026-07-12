package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ImpactoComercioResponse;
import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImpactoServiceComercioTest {

    @Mock
    private ImpactoMetricaJpaRepository jpaRepository;

    @Mock
    private IRepositorioComercio repositorioComercio;

    @Mock
    private LoteJpaRepository loteJpaRepository;

    private ImpactoService impactoService;

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", -0.180653, -78.467834);
    }

    private Comercio comercioValido() {
        return Comercio.crear("1792146739001", "Supermercado El Ahorro", "0991234567",
                "contacto@elahorro.com", direccionValida());
    }

    private void construirServicio() {
        impactoService = new ImpactoService(jpaRepository, repositorioComercio, loteJpaRepository);
    }

    private void mockearSumatoriasVacias() {
        when(jpaRepository.sumCantidadKgByComercio(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        when(jpaRepository.sumCo2ByComercio(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        when(jpaRepository.sumPersonasByComercio(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        when(jpaRepository.sumCantidadKgMesActualByComercio(org.mockito.ArgumentMatchers.any())).thenReturn(null);
    }

    @Test
    void deberiaRetornarComercioIdCorrecto() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(5L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals(comercioId, response.comercioId());
    }

    @Test
    void deberiaRetornarCeroKgCuandoNoHayEntregas() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(0L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals(0.0, response.totalKgRescatados());
    }

    @Test
    void deberiaAsignarTituloHeroeDelMesConCincuentaOMasLotes() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(50L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals("Heroe del Mes", response.tituloLogro());
    }

    @Test
    void deberiaAsignarTituloCampeonFoodLinkConVeinteOMasLotes() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(20L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals("Campeon FoodLink", response.tituloLogro());
    }

    @Test
    void deberiaAsignarTituloColaboradorDestacadoConDiezOMasLotes() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(10L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals("Colaborador Destacado", response.tituloLogro());
    }

    @Test
    void deberiaAsignarTituloPrimerPasoConUnLote() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(1L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals("Primer Paso", response.tituloLogro());
    }

    @Test
    void deberiaRetornarNombreComercioDelRepositorio() {
        UUID comercioId = UUID.randomUUID();
        when(repositorioComercio.buscarPorId(comercioId)).thenReturn(Optional.of(comercioValido()));
        when(jpaRepository.countLotesByComercio(comercioId)).thenReturn(1L);
        mockearSumatoriasVacias();
        construirServicio();

        ImpactoComercioResponse response = impactoService.consultarImpactoComercio(comercioId);

        assertEquals("Supermercado El Ahorro", response.nombreComercio());
    }
}
