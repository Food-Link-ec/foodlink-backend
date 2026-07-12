package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.ImpactoDashboardResponse;
import com.foodlink.infrastructure.adapter.output.persistence.ImpactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/impacto")
public class ImpactoController {

    private final ImpactoService impactoService;

    public ImpactoController(ImpactoService impactoService) {
        this.impactoService = impactoService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ImpactoDashboardResponse> dashboard() {
        return ResponseEntity.ok(impactoService.consultarDashboard());
    }
}