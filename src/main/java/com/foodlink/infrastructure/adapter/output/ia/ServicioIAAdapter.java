package com.foodlink.infrastructure.adapter.output.ia;

import com.foodlink.application.dto.request.AnalizarImagenRequest;
import com.foodlink.application.dto.response.AnalisisImagenResponse;
import com.foodlink.domain.port.output.IServicioIA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ServicioIAAdapter implements IServicioIA {

    private final TesseractOcrAdapter tesseractOcrAdapter;
    private final GeminiVisionAdapter geminiVisionAdapter;

    @Value("${foodlink.ia.ocr-confidence-threshold:85}")
    private int umbralConfianza;

    public ServicioIAAdapter(TesseractOcrAdapter tesseractOcrAdapter, GeminiVisionAdapter geminiVisionAdapter) {
        this.tesseractOcrAdapter = tesseractOcrAdapter;
        this.geminiVisionAdapter = geminiVisionAdapter;
    }

    @Override
    public AnalisisImagenResponse analizarImagen(AnalizarImagenRequest request) {
        byte[] imagenBytes = Base64.getDecoder().decode(request.imagenBase64());

        TesseractOcrAdapter.TesseractResultado resultadoTesseract = tesseractOcrAdapter.extraerTexto(imagenBytes);

        if (resultadoTesseract.exitoso() && resultadoTesseract.tieneFecha()) {
            return new AnalisisImagenResponse(
                    resultadoTesseract.fecha(),
                    null,
                    null,
                    "ALTA",
                    "TESSERACT",
                    "Fecha detectada mediante OCR local"
            );
        }

        GeminiVisionAdapter.GeminiResultado resultadoGemini = geminiVisionAdapter.analizarImagen(request.imagenBase64(), request.mimeType());

        if (resultadoGemini.exitoso()) {
            return new AnalisisImagenResponse(
                    resultadoGemini.fecha(),
                    resultadoGemini.categoria(),
                    resultadoGemini.descripcion(),
                    resultadoGemini.confianza() != null ? resultadoGemini.confianza().toUpperCase() : "NO_DETECTADA",
                    "GEMINI",
                    "Analisis generado mediante Gemini Vision"
            );
        }

        return new AnalisisImagenResponse(
                null,
                null,
                null,
                "NO_DETECTADA",
                "NINGUNO",
                "No fue posible analizar la imagen: " + resultadoGemini.error()
        );
    }
}
