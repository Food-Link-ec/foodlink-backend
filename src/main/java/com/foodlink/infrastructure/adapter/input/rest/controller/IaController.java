package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.AnalizarImagenRequest;
import com.foodlink.application.dto.response.AnalisisImagenResponse;
import com.foodlink.domain.port.output.IServicioIA;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1/ia")
public class IaController {

    private final IServicioIA servicioIA;

    public IaController(IServicioIA servicioIA) {
        this.servicioIA = servicioIA;
    }

    @PostMapping("/analizar-imagen")
    public ResponseEntity<AnalisisImagenResponse> analizarImagen(@RequestParam MultipartFile imagen) throws IOException {
        String imagenBase64 = Base64.getEncoder().encodeToString(imagen.getBytes());
        AnalizarImagenRequest request = new AnalizarImagenRequest(imagenBase64, imagen.getContentType());
        return ResponseEntity.ok(servicioIA.analizarImagen(request));
    }
}
