package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.lote.EstadoLote;
import com.foodlink.domain.model.lote.FabricaLote;
import com.foodlink.domain.model.lote.LoteExcedente;
import com.foodlink.domain.model.lote.Modalidad;
import com.foodlink.domain.model.shared.Dinero;
import com.foodlink.domain.model.shared.FechaCaducidad;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.LoteMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = PostgresRepositorioLoteTest.ConfiguracionPrueba.class)
@Import({LoteMapper.class, PostgresRepositorioLote.class})
class PostgresRepositorioLoteTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class ConfiguracionPrueba {
    }

    @Autowired
    private PostgresRepositorioLote repositorio;

    private LoteExcedente loteDeDonacion(UUID comercioId) {
        return FabricaLote.crearParaDonacion(
                comercioId,
                5.0,
                FechaCaducidad.de(LocalDateTime.now().plusDays(3)),
                "Pan integral de prueba",
                List.of("https://ejemplo.com/foto.jpg")
        );
    }

    @Test
    void guardarDeberiaPersistirUnLoteYRetornarloConId() {
        LoteExcedente lote = loteDeDonacion(UUID.randomUUID());

        LoteExcedente guardado = repositorio.guardar(lote);

        assertNotNull(guardado.getId());
        assertEquals(lote.getId(), guardado.getId());
        assertEquals(EstadoLote.BORRADOR, guardado.getEstado());
    }

    @Test
    void buscarPorIdDeberiaRetornarElLoteCorrecto() {
        LoteExcedente lote = loteDeDonacion(UUID.randomUUID());
        repositorio.guardar(lote);

        Optional<LoteExcedente> encontrado = repositorio.buscarPorId(lote.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(lote.getId(), encontrado.get().getId());
        assertEquals("Pan integral de prueba", encontrado.get().getDescripcion());
    }

    @Test
    void buscarDisponiblesDeberiaRetornarSoloLotesEnEstadoDisponible() {
        LoteExcedente loteDisponible = loteDeDonacion(UUID.randomUUID());
        loteDisponible.publicar();
        repositorio.guardar(loteDisponible);

        LoteExcedente loteBorrador = loteDeDonacion(UUID.randomUUID());
        repositorio.guardar(loteBorrador);

        List<LoteExcedente> disponibles = repositorio.buscarDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals(loteDisponible.getId(), disponibles.get(0).getId());
    }

    @Test
    void buscarPorComercioDeberiaRetornarLotesFiltradosPorComercioId() {
        UUID comercioId = UUID.randomUUID();
        LoteExcedente lote = loteDeDonacion(comercioId);
        repositorio.guardar(lote);
        repositorio.guardar(loteDeDonacion(UUID.randomUUID()));

        List<LoteExcedente> lotes = repositorio.buscarPorComercio(comercioId);

        assertEquals(1, lotes.size());
        assertEquals(comercioId, lotes.get(0).getComercioId());
    }

    @Test
    void buscarPorEstadoExpiradoDeberiaRetornarLotesExpirados() {
        LoteExcedente loteExpirado = LoteExcedente.reconstituir(
                UUID.randomUUID(), UUID.randomUUID(), Modalidad.DONACION, EstadoLote.EXPIRADO, 5.0,
                Dinero.cero(), FechaCaducidad.de(LocalDateTime.now().plusDays(1)), null,
                "Pan integral de prueba", List.of("https://ejemplo.com/foto.jpg"), null, null, null, null);
        repositorio.guardar(loteExpirado);
        repositorio.guardar(loteDeDonacion(UUID.randomUUID()));

        List<LoteExcedente> expirados = repositorio.buscarPorEstado(EstadoLote.EXPIRADO);

        assertEquals(1, expirados.size());
        assertEquals(loteExpirado.getId(), expirados.get(0).getId());
    }

    @Test
    void guardarDeberiaActualizarElEstadoCuandoElLoteCambia() {
        LoteExcedente lote = loteDeDonacion(UUID.randomUUID());
        repositorio.guardar(lote);

        lote.publicar();
        repositorio.guardar(lote);

        Optional<LoteExcedente> actualizado = repositorio.buscarPorId(lote.getId());

        assertTrue(actualizado.isPresent());
        assertEquals(EstadoLote.DISPONIBLE, actualizado.get().getEstado());
    }
}