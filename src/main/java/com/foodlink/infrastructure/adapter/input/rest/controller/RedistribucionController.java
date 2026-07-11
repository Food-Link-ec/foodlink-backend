package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.CancelarReservaRequest;
import com.foodlink.application.dto.request.ReservarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.port.input.ReservarLoteUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/redistribucion")
public class RedistribucionController {

    private final ReservarLoteUseCase reservarLoteUseCase;

    public RedistribucionController(ReservarLoteUseCase reservarLoteUseCase) {
        this.reservarLoteUseCase = reservarLoteUseCase;
    }

    @PostMapping("/reservar")
    public ResponseEntity<LoteResponse> reservar(
            @Valid @RequestBody ReservarLoteRequest request,
            @CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(
                reservarLoteUseCase.reservar(request, usuario.getUsuarioId()));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<LoteResponse> cancelar(
            @Valid @RequestBody CancelarReservaRequest request) {
        return ResponseEntity.ok(
                reservarLoteUseCase.cancelarReserva(request));
    }
}