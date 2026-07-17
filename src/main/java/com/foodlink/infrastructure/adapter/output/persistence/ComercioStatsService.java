package com.foodlink.infrastructure.adapter.output.persistence;

import com.foodlink.application.dto.response.DashboardComercioResponse;
import com.foodlink.application.dto.response.DashboardComercioResponse.LoteResumenDto;
import com.foodlink.application.dto.response.DashboardComercioResponse.ReservaUrgentDto;
import com.foodlink.infrastructure.adapter.output.persistence.repository.ImpactoMetricaJpaRepository;
import com.foodlink.infrastructure.adapter.output.persistence.repository.LoteJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ComercioStatsService {

    private final LoteJpaRepository loteJpaRepository;
    private final ImpactoMetricaJpaRepository impactoMetricaJpaRepository;

    public ComercioStatsService(LoteJpaRepository loteJpaRepository,
                                 ImpactoMetricaJpaRepository impactoMetricaJpaRepository) {
        this.loteJpaRepository = loteJpaRepository;
        this.impactoMetricaJpaRepository = impactoMetricaJpaRepository;
    }

    public DashboardComercioResponse obtenerDashboard(UUID comercioId, String nombreComercio) {
        // Ingresos
        Double ingresosActual = loteJpaRepository.sumIngresosMesActualByComercio(comercioId);
        Double ingresosAnterior = loteJpaRepository.sumIngresosMesAnteriorByComercio(comercioId);
        double mesActual = ingresosActual != null ? ingresosActual : 0.0;
        double mesAnterior = ingresosAnterior != null ? ingresosAnterior : 0.0;
        double pct = mesAnterior > 0
                ? Math.round(((mesActual - mesAnterior) / mesAnterior) * 100 * 10.0) / 10.0
                : 0.0;

        // Kg e impacto
        Double kg = impactoMetricaJpaRepository.sumCantidadKgByComercio(comercioId);
        Double co2 = impactoMetricaJpaRepository.sumCo2ByComercio(comercioId);
        Integer personasMes = impactoMetricaJpaRepository.sumPersonasByComercio(comercioId);
        Long lotesEntregados = impactoMetricaJpaRepository.countLotesByComercio(comercioId);

        double totalKg = kg != null ? Math.round(kg * 10.0) / 10.0 : 0.0;
        double totalCo2 = co2 != null ? Math.round(co2 * 10.0) / 10.0 : 0.0;
        int personas = personasMes != null ? personasMes : 0;
        long entregados = lotesEntregados != null ? lotesEntregados : 0L;

        // Reservas pendientes
        Long reservasPendientes = loteJpaRepository.countReservasPendientesByComercio(comercioId);
        long pendientes = reservasPendientes != null ? reservasPendientes : 0L;

        Long lotesActivos = loteJpaRepository.countLotesActivosByComercio(comercioId);
        long activos = lotesActivos != null ? lotesActivos : 0L;

        // Lotes recientes
        List<Object[]> lotesRaw = loteJpaRepository.findLotesRecientesByComercio(comercioId);
        List<LoteResumenDto> lotesRecientes = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
        for (Object[] row : lotesRaw) {
            String id = row[0] != null ? row[0].toString() : "";
            String nombre = row[1] != null ? row[1].toString() : "";
            double cantKg = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            String cantidad = cantKg + " kg";
            LocalDateTime actualizado = row[3] != null
                    ? LocalDateTime.parse(row[3].toString().replace(" ", "T").substring(0, 19))
                    : LocalDateTime.now();
            long horasAtras = ChronoUnit.HOURS.between(actualizado, LocalDateTime.now());
            String hace = horasAtras < 24
                    ? "Hace " + horasAtras + " h"
                    : actualizado.format(fmt);
            String estado = row[4] != null ? row[4].toString().toLowerCase() : "activo";
            String foto = row[5] != null ? row[5].toString() : "";
            lotesRecientes.add(new LoteResumenDto(id, nombre, cantidad, hace, estado, foto));
        }

        // Reservas urgentes de donación
        List<Object[]> urgentesRaw = loteJpaRepository.findReservasUrgentesDonacionByComercio(comercioId);
        List<ReservaUrgentDto> reservasUrgentes = new ArrayList<>();
        for (Object[] row : urgentesRaw) {
            String loteId = row[0] != null ? row[0].toString() : "";
            String producto = row[1] != null ? row[1].toString() : "";
            double cantKg = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            LocalDateTime caducidad = row[3] != null
                    ? LocalDateTime.parse(row[3].toString().replace(" ", "T").substring(0, 19))
                    : LocalDateTime.now();
            long horasHastaCaducidad = ChronoUnit.HOURS.between(LocalDateTime.now(), caducidad);
            String hora = horasHastaCaducidad <= 0
                    ? "Caduca pronto"
                    : horasHastaCaducidad < 24
                        ? "Hoy, vence en " + horasHastaCaducidad + " h"
                        : "Mañana, " + caducidad.format(DateTimeFormatter.ofPattern("HH:mm"));
            String org = row[4] != null ? row[4].toString() : "Organización";
            reservasUrgentes.add(new ReservaUrgentDto(loteId, org, producto + " · " + cantKg + " kg", hora));
        }

        // Logro
        String titulo = calcularTitulo(entregados);
        String descripcion = calcularDescripcion(titulo, totalKg, personas);

        return new DashboardComercioResponse(
                comercioId, nombreComercio,
                Math.round(mesActual * 100.0) / 100.0,
                Math.round(mesAnterior * 100.0) / 100.0,
                pct,
                totalKg,
                pendientes,
                activos,
                personas,
                lotesRecientes,
                reservasUrgentes,
                totalCo2,
                entregados,
                titulo,
                descripcion
        );
    }

    private String calcularTitulo(long lotes) {
        if (lotes >= 50) return "Héroe del Mes";
        if (lotes >= 20) return "Campeón FoodLink";
        if (lotes >= 10) return "Colaborador Destacado";
        if (lotes >= 1) return "Primer Paso";
        return "Comercio Registrado";
    }

    private String calcularDescripcion(String titulo, double kg, int personas) {
        return switch (titulo) {
            case "Héroe del Mes" -> "Has ayudado a proveer " + (int)(personas * 0.5) + " raciones a fundaciones aliadas en Quito.";
            case "Campeón FoodLink" -> "Tu compromiso ha rescatado " + String.format("%.1f", kg) + " kg de alimentos.";
            case "Colaborador Destacado" -> "Gracias por tu contribución. " + personas + " personas te lo agradecen.";
            default -> "Cada kilogramo rescatado marca la diferencia.";
        };
    }
}
