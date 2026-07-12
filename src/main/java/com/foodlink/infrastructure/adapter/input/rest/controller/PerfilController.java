package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.application.dto.response.MiPerfilResponse;
import com.foodlink.domain.port.input.MiPerfilUseCase;
import com.foodlink.domain.port.input.MisLotesUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PerfilController {

    private final MiPerfilUseCase miPerfilUseCase;
    private final MisLotesUseCase misLotesUseCase;

    public PerfilController(MiPerfilUseCase miPerfilUseCase, MisLotesUseCase misLotesUseCase) {
        this.miPerfilUseCase = miPerfilUseCase;
        this.misLotesUseCase = misLotesUseCase;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<MiPerfilResponse> miPerfil(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(
                miPerfilUseCase.obtenerMiPerfil(usuario.getUsuarioId(), usuario.getTipoUsuario()));
    }

    @GetMapping("/comercios/mis-lotes")
    public ResponseEntity<List<LoteResponse>> misLotes(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(misLotesUseCase.obtenerMisLotes(usuario.getUsuarioId()));
    }

    @GetMapping("/redistribucion/mis-reservas")
    public ResponseEntity<List<LoteResponse>> misReservas(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(misLotesUseCase.obtenerMisReservas(usuario.getUsuarioId()));
    }
}
