package com.foodlink.infrastructure.adapter.output.ia;

import com.foodlink.application.dto.request.AnalizarImagenRequest;
import com.foodlink.application.dto.response.AnalisisImagenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioIAAdapterTest {

    @Mock
    private TesseractOcrAdapter tesseractOcrAdapter;

    @Mock
    private GeminiVisionAdapter geminiVisionAdapter;

    @InjectMocks
    private ServicioIAAdapter servicioIAAdapter;

    private AnalizarImagenRequest requestValido() {
        String base64 = Base64.getEncoder().encodeToString("imagen-simulada".getBytes());
        return new AnalizarImagenRequest(base64, "image/png");
    }

    @Test
    void deberiaRetornarFuenteTesseractCuandoDetectaFechaExitosamente() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(
                TesseractOcrAdapter.TesseractResultado.exitoso("12/05/2026", "texto con fecha 12/05/2026"));

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("12/05/2026", respuesta.fechaDetectada());
        assertEquals("TESSERACT", respuesta.fuenteOcr());
        verify(geminiVisionAdapter, never()).analizarImagen(anyString(), anyString());
    }

    @Test
    void deberiaUsarFallbackGeminiCuandoTesseractFalla() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(TesseractOcrAdapter.TesseractResultado.fallido());
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.exitoso("01/06/2026", "Lacteos", "Leche entera", "alta"));

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("GEMINI", respuesta.fuenteOcr());
        assertEquals("01/06/2026", respuesta.fechaDetectada());
    }

    @Test
    void deberiaUsarFallbackGeminiCuandoTesseractNoDetectaFecha() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(
                TesseractOcrAdapter.TesseractResultado.sinFecha("texto sin fecha"));
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.exitoso("15/07/2026", "Panaderia", "Pan integral", "media"));

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("GEMINI", respuesta.fuenteOcr());
        assertEquals("Panaderia", respuesta.categoriaProducto());
    }

    @Test
    void deberiaIncluirCategoriaYDescripcionCuandoGeminiResuelveConExito() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(TesseractOcrAdapter.TesseractResultado.fallido());
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.exitoso("20/08/2026", "Frutas", "Manzanas rojas", "alta"));

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("Frutas", respuesta.categoriaProducto());
        assertEquals("Manzanas rojas", respuesta.descripcionSugerida());
        assertEquals("ALTA", respuesta.confianzaFecha());
    }

    @Test
    void deberiaRetornarMensajeDeFalloCuandoTesseractYGeminiFallan() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(TesseractOcrAdapter.TesseractResultado.fallido());
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.error("timeout"));

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("NINGUNO", respuesta.fuenteOcr());
        assertNotNull(respuesta.mensaje());
    }

    @Test
    void deberiaDecodificarBase64AntesDeInvocarTesseract() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(
                TesseractOcrAdapter.TesseractResultado.exitoso("01/01/2027", "texto"));

        servicioIAAdapter.analizarImagen(requestValido());

        verify(tesseractOcrAdapter).extraerTexto("imagen-simulada".getBytes());
    }

    @Test
    void deberiaPropagarMimeTypeDelRequestAGemini() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(TesseractOcrAdapter.TesseractResultado.fallido());
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.error("sin datos"));

        AnalizarImagenRequest request = requestValido();
        servicioIAAdapter.analizarImagen(request);

        verify(geminiVisionAdapter).analizarImagen(request.imagenBase64(), "image/png");
    }

    @Test
    void deberiaRetornarMensajeSinConfiguracionCuandoGeminiNoTieneApiKey() {
        when(tesseractOcrAdapter.extraerTexto(any())).thenReturn(TesseractOcrAdapter.TesseractResultado.fallido());
        when(geminiVisionAdapter.analizarImagen(anyString(), anyString())).thenReturn(
                GeminiVisionAdapter.GeminiResultado.sinConfiguracion());

        AnalisisImagenResponse respuesta = servicioIAAdapter.analizarImagen(requestValido());

        assertEquals("NINGUNO", respuesta.fuenteOcr());
        assertEquals("No fue posible analizar la imagen: Gemini API key no configurada", respuesta.mensaje());
    }
}
