package com.foodlink.infrastructure.adapter.output.pdf;

import com.foodlink.application.dto.response.ImpactoComercioResponse;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ImpactoPdfGenerator {

    public byte[] generarReporte(ImpactoComercioResponse impacto) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Color verde = new DeviceRgb(29, 106, 79);
            Color verdeOscuro = new DeviceRgb(20, 51, 42);

            Paragraph titulo = new Paragraph("FoodLink")
                    .setFontSize(32)
                    .setBold()
                    .setFontColor(verdeOscuro)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(titulo);

            document.add(new Paragraph("Certificado de Impacto Social y Ambiental")
                    .setFontSize(14)
                    .setFontColor(verde)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30));

            document.add(new Paragraph(impacto.tituloLogro().toUpperCase())
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(verde)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.add(new Paragraph(impacto.descripcionLogro())
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30));

            document.add(new Paragraph("Comercio: " + impacto.nombreComercio())
                    .setFontSize(12)
                    .setBold()
                    .setMarginBottom(5));

            document.add(new Paragraph("Fecha de emisión: " +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setFontSize(11)
                    .setMarginBottom(25));

            agregarMetrica(document, verde, "Kilogramos rescatados",
                    String.format("%.1f kg", impacto.totalKgRescatados()));
            agregarMetrica(document, verde, "CO₂ evitado",
                    String.format("%.1f kg", impacto.totalCo2EvitadoKg()));
            agregarMetrica(document, verde, "Personas beneficiadas",
                    String.valueOf(impacto.totalPersonasBeneficiadas()));
            agregarMetrica(document, verde, "Lotes redistribuidos",
                    String.valueOf(impacto.totalLotesEntregados()));
            agregarMetrica(document, verde, "Kg rescatados este mes",
                    String.format("%.1f kg", impacto.kgRescatadosMesActual()));

            document.add(new Paragraph("\n" + impacto.mensajeImpacto())
                    .setFontSize(12)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(verdeOscuro)
                    .setMarginTop(20));

            document.add(new Paragraph("\nEste certificado acredita la participación activa de " +
                    impacto.nombreComercio() +
                    " en la reducción del desperdicio alimentario en el " +
                    "Distrito Metropolitano de Quito a través de la plataforma FoodLink.")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de impacto: " + e.getMessage(), e);
        }
    }

    private void agregarMetrica(Document doc, Color color, String etiqueta, String valor) {
        Table table = new Table(2)
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(8);

        Cell celdaEtiqueta = new Cell()
                .add(new Paragraph(etiqueta).setFontSize(11))
                .setBorder(Border.NO_BORDER)
                .setPadding(8);

        Cell celdaValor = new Cell()
                .add(new Paragraph(valor)
                        .setFontSize(14)
                        .setBold()
                        .setFontColor(color))
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                .setPadding(8)
                .setTextAlignment(TextAlignment.RIGHT);

        table.addCell(celdaEtiqueta);
        table.addCell(celdaValor);
        doc.add(table);
    }
}
