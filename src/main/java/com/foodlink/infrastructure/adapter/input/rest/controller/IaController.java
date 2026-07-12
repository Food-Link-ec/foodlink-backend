package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.AnalizarImagenRequest;
import com.foodlink.application.dto.response.AnalisisImagenResponse;
import com.foodlink.application.dto.response.SugerenciaPublicacionResponse;
import com.foodlink.domain.port.output.IServicioIA;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @PostMapping("/sugerir-publicacion")
    public ResponseEntity<SugerenciaPublicacionResponse> sugerirPublicacion(
            @RequestParam("imagen") MultipartFile imagen,
            @RequestHeader(value = "Authorization") String auth) {
        try {
            byte[] bytes = imagen.getBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String mimeType = imagen.getContentType() != null ? imagen.getContentType() : "image/jpeg";

            AnalizarImagenRequest request = new AnalizarImagenRequest(base64, mimeType);
            AnalisisImagenResponse analisis = servicioIA.analizarImagen(request);

            return ResponseEntity.ok(new SugerenciaPublicacionResponse(
                    analisis.fechaDetectada(),
                    analisis.categoriaProducto(),
                    analisis.descripcionSugerida(),
                    analisis.confianzaFecha(),
                    analisis.fuenteOcr(),
                    "Completa o corrige los datos sugeridos antes de publicar."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new SugerenciaPublicacionResponse(
                            null, null, null, "NO_DETECTADA", "NINGUNO",
                            "Error procesando imagen: " + e.getMessage()));
        }
    }
}
