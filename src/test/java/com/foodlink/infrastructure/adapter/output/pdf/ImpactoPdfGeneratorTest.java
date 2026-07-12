package com.foodlink.infrastructure.adapter.output.pdf;

import com.foodlink.application.dto.response.ImpactoComercioResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImpactoPdfGeneratorTest {

    private final ImpactoPdfGenerator generator = new ImpactoPdfGenerator();

    private ImpactoComercioResponse respuesta(long lotes, String titulo) {
        return new ImpactoComercioResponse(
                UUID.randomUUID(),
                "Comercio de Prueba",
                50.0, 125.0, 100, lotes, 10.0,
                titulo,
                "Descripcion de logro",
                "Comercio de Prueba ha rescatado 50.0 kg de alimentos beneficiando a 100 personas."
        );
    }

    private String extraerTextoDePrimeraPagina(byte[] pdfBytes) throws Exception {
        try (PdfDocument pdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)))) {
            return PdfTextExtractor.getTextFromPage(pdf.getPage(1));
        }
    }

    @Test
    void deberiaRetornarByteArrayNoVacio() {
        byte[] pdf = generator.generarReporte(respuesta(50, "Heroe del Mes"));

        assertTrue(pdf.length > 0);
    }

    @Test
    void deberiaRetornarUnPdfValidoConEncabezadoCorrecto() {
        byte[] pdf = generator.generarReporte(respuesta(50, "Heroe del Mes"));
        String encabezado = new String(pdf, 0, 4);

        assertTrue(encabezado.equals("%PDF"));
    }

    @Test
    void deberiaIncluirTituloHeroeDelMesCuandoLotesSuperanCincuenta() throws Exception {
        byte[] pdf = generator.generarReporte(respuesta(50, "Heroe del Mes"));
        String texto = extraerTextoDePrimeraPagina(pdf);

        assertTrue(texto.contains("HEROE DEL MES"));
    }

    @Test
    void noDeberiaLanzarExcepcionCuandoNoHayLotes() {
        assertDoesNotThrow(() -> generator.generarReporte(respuesta(0, "Comercio Registrado")));
    }
}
