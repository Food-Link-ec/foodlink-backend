package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.comercio.Comercio;
import com.foodlink.domain.model.comercio.EstadoComercio;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.ComercioMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ComercioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = PostgresRepositorioComercioTest.ConfiguracionPrueba.class)
class PostgresRepositorioComercioTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class ConfiguracionPrueba {
    }

    private static final String RUC_PENDIENTE = "1792146739001";
    private static final String RUC_VERIFICADO = "0961234567001";

    @Autowired
    private ComercioJpaRepository comercioJpaRepository;

    private PostgresRepositorioComercio repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new PostgresRepositorioComercio(comercioJpaRepository, new ComercioMapper());
    }

    private Direccion direccionValida() {
        return new Direccion("Av. Amazonas N34-451", "Quito", "Pichincha", "Cerca del parque", null, null);
    }

    @Test
    void guardarDeberiaPersistirComercioYRetornarloConId() {
        Comercio comercio = Comercio.crear(RUC_PENDIENTE, "Supermercado El Ahorro", "0991234567",
                "contacto1@elahorro.com", direccionValida());

        Comercio guardado = repositorio.guardar(comercio);

        assertNotNull(guardado.getId());
        assertEquals(RUC_PENDIENTE, guardado.getRuc());
        assertEquals("Supermercado El Ahorro", guardado.getNombre());
    }

    @Test
    void buscarPorRucDeberiaRetornarElComercioCorrecto() {
        Comercio comercio = Comercio.crear(RUC_PENDIENTE, "Supermercado El Ahorro", "0991234567",
                "contacto1@elahorro.com", direccionValida());
        repositorio.guardar(comercio);

        Optional<Comercio> encontrado = repositorio.buscarPorRuc(RUC_PENDIENTE);

        assertTrue(encontrado.isPresent());
        assertEquals(RUC_PENDIENTE, encontrado.get().getRuc());
    }

    @Test
    void existePorRucDeberiaRetornarTrueSiExisteFalseSiNo() {
        Comercio comercio = Comercio.crear(RUC_PENDIENTE, "Supermercado El Ahorro", "0991234567",
                "contacto1@elahorro.com", direccionValida());
        repositorio.guardar(comercio);

        assertTrue(repositorio.existePorRuc(RUC_PENDIENTE));
        assertFalse(repositorio.existePorRuc(RUC_VERIFICADO));
    }

    @Test
    void buscarPorEstadoDeberiaRetornarSoloLosComerciosConEseEstado() {
        Comercio comercioPendiente = Comercio.crear(RUC_PENDIENTE, "Supermercado El Ahorro", "0991234567",
                "contacto1@elahorro.com", direccionValida());
        repositorio.guardar(comercioPendiente);

        Comercio comercioVerificado = Comercio.crear(RUC_VERIFICADO, "Panadería San José", "0987654321",
                "contacto2@elahorro.com", direccionValida());
        comercioVerificado.verificar();
        repositorio.guardar(comercioVerificado);

        List<Comercio> pendientes = repositorio.buscarPorEstado(EstadoComercio.PENDIENTE_VERIFICACION);

        assertEquals(1, pendientes.size());
        assertEquals(RUC_PENDIENTE, pendientes.get(0).getRuc());
    }
}