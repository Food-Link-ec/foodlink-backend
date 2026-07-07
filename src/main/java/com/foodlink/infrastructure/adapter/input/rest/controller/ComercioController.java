package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.RegistrarComercioRequest;
import com.foodlink.application.dto.response.ComercioResponse;
import com.foodlink.domain.port.input.RegistrarComercioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comercios")
public class ComercioController {

    private final RegistrarComercioUseCase registrarComercioUseCase;

    public ComercioController(RegistrarComercioUseCase registrarComercioUseCase) {
        this.registrarComercioUseCase = registrarComercioUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ComercioResponse> registrar(@Valid @RequestBody RegistrarComercioRequest request) {
        ComercioResponse response = registrarComercioUseCase.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
