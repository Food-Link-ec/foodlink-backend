package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.EstadisticasCompradorResponse;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.application.dto.response.MiPerfilResponse;
import com.foodlink.domain.port.input.MiPerfilUseCase;
import com.foodlink.domain.port.input.MisLotesUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.adapter.output.persistence.CompradorStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Perfil", description = "Datos personales y actividad del usuario autenticado")
public class PerfilController {

    private final MiPerfilUseCase miPerfilUseCase;
    private final MisLotesUseCase misLotesUseCase;
    private final CompradorStatsService compradorStatsService;

    public PerfilController(MiPerfilUseCase miPerfilUseCase, MisLotesUseCase misLotesUseCase,
                             CompradorStatsService compradorStatsService) {
        this.miPerfilUseCase = miPerfilUseCase;
        this.misLotesUseCase = misLotesUseCase;
        this.compradorStatsService = compradorStatsService;
    }

    @Operation(summary = "Ver mi perfil")
    @GetMapping("/auth/me")
    public ResponseEntity<MiPerfilResponse> miPerfil(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(
                miPerfilUseCase.obtenerMiPerfil(usuario.getUsuarioId(), usuario.getTipoUsuario()));
    }

    @Operation(summary = "Ver mis lotes publicados")
    @GetMapping("/comercios/mis-lotes")
    public ResponseEntity<List<LoteResponse>> misLotes(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(misLotesUseCase.obtenerMisLotes(usuario.getUsuarioId()));
    }

    @Operation(summary = "Ver mis reservas activas")
    @GetMapping("/redistribucion/mis-reservas")
    public ResponseEntity<List<LoteResponse>> misReservas(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(misLotesUseCase.obtenerMisReservas(usuario.getUsuarioId()));
    }

    @Operation(summary = "Ver mis estadísticas de compras",
            description = "Retorna lotes comprados, total pagado, kg adquiridos y ahorro estimado.")
    @GetMapping("/compradores/mis-estadisticas")
    public ResponseEntity<EstadisticasCompradorResponse> misEstadisticas(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(compradorStatsService.obtenerEstadisticas(usuario.getUsuarioId()));
    }
}
