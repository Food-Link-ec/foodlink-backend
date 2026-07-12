package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.ResumenValoracionesResponse;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.adapter.output.persistence.ValoracionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/valoraciones")
@Tag(name = "Valoraciones", description = "Calificaciones de comercios")
public class ValoracionController {

    private final ValoracionService valoracionService;

    public ValoracionController(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
    }

    @Operation(summary = "Valoraciones de un comercio", description = "Público. Retorna promedio de estrellas y lista de comentarios.")
    @GetMapping("/comercio/{comercioId}")
    public ResponseEntity<ResumenValoracionesResponse> valoracionesComercio(@PathVariable UUID comercioId) {
        return ResponseEntity.ok(valoracionService.obtenerValoraciones(comercioId));
    }

    @Operation(summary = "Mis valoraciones")
    @GetMapping("/mis-valoraciones")
    public ResponseEntity<ResumenValoracionesResponse> misValoraciones(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(valoracionService.obtenerMisValoraciones(usuario.getUsuarioId()));
    }
}
