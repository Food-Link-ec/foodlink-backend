package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.comprador.Comprador;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.CompradorMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.CompradorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = PostgresRepositorioCompradorTest.ConfiguracionPrueba.class)
class PostgresRepositorioCompradorTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class ConfiguracionPrueba {
    }

    private static final String CEDULA_REGISTRADA = "1710034065";
    private static final String CEDULA_NO_REGISTRADA = "1710034073";

    @Autowired
    private CompradorJpaRepository compradorJpaRepository;

    private PostgresRepositorioComprador repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new PostgresRepositorioComprador(compradorJpaRepository, new CompradorMapper());
    }

    @Test
    void guardarDeberiaPersistirCompradorYRetornarloConId() {
        Comprador comprador = Comprador.registrar(CEDULA_REGISTRADA, "Carlos", "Mendoza",
                "carlos.mendoza@gmail.com", "0991122334");

        Comprador guardado = repositorio.guardar(comprador);

        assertNotNull(guardado.getId());
        assertEquals(CEDULA_REGISTRADA, guardado.getCedula());
        assertEquals("Carlos", guardado.getNombre());
        assertTrue(guardado.estaActivo());
    }

    @Test
    void buscarPorCedulaDeberiaRetornarElCompradorCorrecto() {
        Comprador comprador = Comprador.registrar(CEDULA_REGISTRADA, "Carlos", "Mendoza",
                "carlos.mendoza@gmail.com", "0991122334");
        repositorio.guardar(comprador);

        Optional<Comprador> encontrado = repositorio.buscarPorCedula(CEDULA_REGISTRADA);

        assertTrue(encontrado.isPresent());
        assertEquals(CEDULA_REGISTRADA, encontrado.get().getCedula());
    }

    @Test
    void existePorCedulaDeberiaRetornarTrueSiExisteFalseSiNo() {
        Comprador comprador = Comprador.registrar(CEDULA_REGISTRADA, "Carlos", "Mendoza",
                "carlos.mendoza@gmail.com", "0991122334");
        repositorio.guardar(comprador);

        assertTrue(repositorio.existePorCedula(CEDULA_REGISTRADA));
        assertFalse(repositorio.existePorCedula(CEDULA_NO_REGISTRADA));
    }

    @Test
    void buscarPorIdDeberiaRetornarElCompradorCorrecto() {
        Comprador comprador = Comprador.registrar(CEDULA_REGISTRADA, "Carlos", "Mendoza",
                "carlos.mendoza@gmail.com", "0991122334");
        Comprador guardado = repositorio.guardar(comprador);

        Optional<Comprador> encontrado = repositorio.buscarPorId(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(CEDULA_REGISTRADA, encontrado.get().getCedula());
        assertEquals("Carlos", encontrado.get().getNombre());
    }
}
