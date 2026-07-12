package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.ImpactoComercioResponse;
import com.foodlink.application.dto.response.ImpactoDashboardResponse;
import com.foodlink.domain.port.output.IRepositorioComercio;
import com.foodlink.infrastructure.adapter.output.persistence.entity.ImpactoMetricaJpaEntity;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ImpactoService {

    private final ImpactoMetricaJpaRepository jpaRepository;
    private final IRepositorioComercio repositorioComercio;
    private final LoteJpaRepository loteJpaRepository;

    public ImpactoService(ImpactoMetricaJpaRepository jpaRepository,
                           IRepositorioComercio repositorioComercio,
                           LoteJpaRepository loteJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.repositorioComercio = repositorioComercio;
        this.loteJpaRepository = loteJpaRepository;
    }

    public void registrar(UUID loteId, double cantidadKg) {
        double co2 = cantidadKg * 2.5;
        int personas = (int) Math.ceil(cantidadKg / 0.5);

        ImpactoMetricaJpaEntity entity = new ImpactoMetricaJpaEntity();
        entity.setLoteId(loteId);
        entity.setCantidadKg(cantidadKg);
        entity.setCo2EvitadoKg(co2);
        entity.setPersonasBeneficiadas(personas);
        entity.setFechaEntrega(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    public ImpactoDashboardResponse consultarDashboard() {
        double kg = jpaRepository.sumCantidadKg() != null
                ? jpaRepository.sumCantidadKg() : 0.0;
        double co2 = jpaRepository.sumCo2EvitadoKg() != null
                ? jpaRepository.sumCo2EvitadoKg() : 0.0;
        int personas = jpaRepository.sumPersonasBeneficiadas() != null
                ? jpaRepository.sumPersonasBeneficiadas() : 0;
        long lotes = jpaRepository.countLotesEntregados() != null
                ? jpaRepository.countLotesEntregados() : 0L;

        return new ImpactoDashboardResponse(
                kg, co2, personas, lotes,
                "FoodLink ha rescatado " + String.format("%.1f", kg) +
                        " kg de alimentos beneficiando a " + personas + " personas."
        );
    }

    public ImpactoComercioResponse consultarImpactoComercio(UUID comercioId) {
        String nombreComercio = repositorioComercio.buscarPorId(comercioId)
                .map(c -> c.getNombre().valor())
                .orElse("Comercio");

        double kg = jpaRepository.sumCantidadKgByComercio(comercioId) != null
                ? jpaRepository.sumCantidadKgByComercio(comercioId) : 0.0;
        double co2 = jpaRepository.sumCo2ByComercio(comercioId) != null
                ? jpaRepository.sumCo2ByComercio(comercioId) : 0.0;
        int personas = jpaRepository.sumPersonasByComercio(comercioId) != null
                ? jpaRepository.sumPersonasByComercio(comercioId) : 0;
        long lotes = jpaRepository.countLotesByComercio(comercioId) != null
                ? jpaRepository.countLotesByComercio(comercioId) : 0L;
        double kgMes = jpaRepository.sumCantidadKgMesActualByComercio(comercioId) != null
                ? jpaRepository.sumCantidadKgMesActualByComercio(comercioId) : 0.0;

        String titulo = calcularTitulo(lotes);
        String descripcion = calcularDescripcion(titulo, kg, personas);

        return new ImpactoComercioResponse(
                comercioId,
                nombreComercio,
                kg, co2, personas, lotes, kgMes,
                titulo,
                descripcion,
                nombreComercio + " ha rescatado " +
                        String.format("%.1f", kg) +
                        " kg de alimentos beneficiando a " + personas + " personas."
        );
    }

    private String calcularTitulo(long lotes) {
        if (lotes >= 50) return "Heroe del Mes";
        if (lotes >= 20) return "Campeon FoodLink";
        if (lotes >= 10) return "Colaborador Destacado";
        if (lotes >= 1) return "Primer Paso";
        return "Comercio Registrado";
    }

    private String calcularDescripcion(String titulo, double kg, int personas) {
        return switch (titulo) {
            case "Heroe del Mes" ->
                    "Has ayudado a proveer " + (int) (personas * 0.5) +
                            " raciones de comida a fundaciones aliadas.";
            case "Campeon FoodLink" ->
                    "Tu compromiso con FoodLink ha rescatado " +
                            String.format("%.1f", kg) + " kg de alimentos.";
            case "Colaborador Destacado" ->
                    "Gracias por tu contribucion. " + personas + " personas te lo agradecen.";
            default ->
                    "Cada kilogramo rescatado marca la diferencia.";
        };
    }
}