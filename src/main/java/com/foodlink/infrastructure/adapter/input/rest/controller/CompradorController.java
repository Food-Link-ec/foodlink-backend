package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.RegistrarCompradorRequest;
import com.foodlink.application.dto.response.CompradorResponse;
import com.foodlink.domain.port.input.RegistrarCompradorUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compradores")
@Tag(name = "Registro", description = "Registro de nuevos actores en la plataforma")
public class CompradorController {

    private final RegistrarCompradorUseCase registrarCompradorUseCase;

    public CompradorController(RegistrarCompradorUseCase registrarCompradorUseCase) {
        this.registrarCompradorUseCase = registrarCompradorUseCase;
    }

    @Operation(summary = "Registrar nuevo comprador")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompradorResponse> registrar(@Valid @RequestBody RegistrarCompradorRequest request) {
        CompradorResponse response = registrarCompradorUseCase.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
