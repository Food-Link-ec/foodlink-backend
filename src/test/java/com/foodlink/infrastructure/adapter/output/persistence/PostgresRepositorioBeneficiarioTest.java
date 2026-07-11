package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.domain.model.beneficiario.Beneficiario;
import com.foodlink.domain.model.shared.Direccion;
import com.foodlink.infrastructure.adapter.output.persistence.mapper.BeneficiarioMapper;
import com.foodlink.infrastructure.adapter.output.persistence.repository.BeneficiarioJpaRepository;
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
@ContextConfiguration(classes = PostgresRepositorioBeneficiarioTest.ConfiguracionPrueba.class)
class PostgresRepositorioBeneficiarioTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class ConfiguracionPrueba {
    }

    private static final String RUC_REGISTRADO = "1710034065001";
    private static final String RUC_NO_REGISTRADO = "0961234567001";

    @Autowired
    private BeneficiarioJpaRepository beneficiarioJpaRepository;

    private PostgresRepositorioBeneficiario repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new PostgresRepositorioBeneficiario(beneficiarioJpaRepository, new BeneficiarioMapper());
    }

    private Direccion direccionValida() {
        return new Direccion("Calle Mejía", "Quito", "Pichincha", null, null, null);
    }

    @Test
    void guardarDeberiaPersistirBeneficiarioYRetornarloConId() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Alimentando Quito", RUC_REGISTRADO,
                "contacto@alimentandoquito.org", "0987654321", direccionValida());

        Beneficiario guardado = repositorio.guardar(beneficiario);

        assertNotNull(guardado.getId());
        assertEquals(RUC_REGISTRADO, guardado.getRuc().valor());
        assertEquals("Fundación Alimentando Quito", guardado.getNombre().valor());
    }

    @Test
    void buscarPorRucDeberiaRetornarElBeneficiarioCorrecto() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Alimentando Quito", RUC_REGISTRADO,
                "contacto@alimentandoquito.org", "0987654321", direccionValida());
        repositorio.guardar(beneficiario);

        Optional<Beneficiario> encontrado = repositorio.buscarPorRuc(RUC_REGISTRADO);

        assertTrue(encontrado.isPresent());
        assertEquals(RUC_REGISTRADO, encontrado.get().getRuc().valor());
    }

    @Test
    void existePorRucDeberiaRetornarTrueSiExisteFalseSiNo() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Alimentando Quito", RUC_REGISTRADO,
                "contacto@alimentandoquito.org", "0987654321", direccionValida());
        repositorio.guardar(beneficiario);

        assertTrue(repositorio.existePorRuc(RUC_REGISTRADO));
        assertFalse(repositorio.existePorRuc(RUC_NO_REGISTRADO));
    }

    @Test
    void buscarPorIdDeberiaRetornarElBeneficiarioCorrecto() {
        Beneficiario beneficiario = Beneficiario.registrar("Fundación Alimentando Quito", RUC_REGISTRADO,
                "contacto@alimentandoquito.org", "0987654321", direccionValida());
        Beneficiario guardado = repositorio.guardar(beneficiario);

        Optional<Beneficiario> encontrado = repositorio.buscarPorId(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(RUC_REGISTRADO, encontrado.get().getRuc().valor());
        assertEquals("Fundación Alimentando Quito", encontrado.get().getNombre().valor());
    }
}
