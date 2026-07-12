package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.ImpactoComercioResponse;
import com.foodlink.application.dto.response.ImpactoDashboardResponse;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.adapter.output.persistence.ImpactoService;
import com.foodlink.infrastructure.adapter.output.pdf.ImpactoPdfGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/impacto")
public class ImpactoController {

    private final ImpactoService impactoService;
    private final ImpactoPdfGenerator pdfGenerator;

    public ImpactoController(ImpactoService impactoService, ImpactoPdfGenerator pdfGenerator) {
        this.impactoService = impactoService;
        this.pdfGenerator = pdfGenerator;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ImpactoDashboardResponse> dashboard() {
        return ResponseEntity.ok(impactoService.consultarDashboard());
    }

    @GetMapping("/comercio/{comercioId}")
    public ResponseEntity<ImpactoComercioResponse> impactoComercio(@PathVariable UUID comercioId) {
        return ResponseEntity.ok(impactoService.consultarImpactoComercio(comercioId));
    }

    @GetMapping("/comercio/{comercioId}/reporte")
    public ResponseEntity<byte[]> descargarReporte(@PathVariable UUID comercioId) {
        ImpactoComercioResponse impacto = impactoService.consultarImpactoComercio(comercioId);
        byte[] pdf = pdfGenerator.generarReporte(impacto);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"reporte-impacto-" + comercioId + ".pdf\"")
                .body(pdf);
    }

    @GetMapping("/mi-impacto")
    public ResponseEntity<ImpactoComercioResponse> miImpacto(@CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity.ok(impactoService.consultarImpactoComercio(usuario.getUsuarioId()));
    }

    @GetMapping("/mi-impacto/reporte")
    public ResponseEntity<byte[]> miReporte(@CurrentUser UsuarioAutenticado usuario) {
        ImpactoComercioResponse impacto = impactoService.consultarImpactoComercio(usuario.getUsuarioId());
        byte[] pdf = pdfGenerator.generarReporte(impacto);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=\"mi-reporte-impacto.pdf\"")
                .body(pdf);
    }
}