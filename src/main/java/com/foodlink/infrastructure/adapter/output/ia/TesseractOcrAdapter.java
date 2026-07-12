package com.foodlink.infrastructure.adapter.output.ia;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TesseractOcrAdapter {

    private static final Pattern PATRON_FECHA = Pattern.compile(
            "(\\d{1,2}[/.-]\\d{1,2}[/.-]\\d{2,4})|(\\d{4}[/.-]\\d{1,2}[/.-]\\d{1,2})"
    );

    private final String tesseractDataPath;

    public TesseractOcrAdapter(@Value("${foodlink.ia.tesseract-data-path:}") String tesseractDataPath) {
        this.tesseractDataPath = tesseractDataPath;
    }

    public TesseractResultado extraerTexto(byte[] imagenBytes) {
        try {
            BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(imagenBytes));
            if (imagen == null) {
                return TesseractResultado.fallido();
            }

            Tesseract tesseract = new Tesseract();
            if (tesseractDataPath != null && !tesseractDataPath.isBlank()) {
                tesseract.setDatapath(tesseractDataPath);
            }

            String texto = tesseract.doOCR(imagen);
            String fecha = extraerFecha(texto);

            if (fecha != null) {
                return TesseractResultado.exitoso(fecha, texto);
            }
            return TesseractResultado.sinFecha(texto);
        } catch (IOException | TesseractException e) {
            return TesseractResultado.fallido();
        }
    }

    String extraerFecha(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }

        Matcher matcher = PATRON_FECHA.matcher(texto);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public record TesseractResultado(boolean exitoso, String fecha, String textoCompleto, boolean tieneFecha) {

        public static TesseractResultado exitoso(String fecha, String texto) {
            return new TesseractResultado(true, fecha, texto, true);
        }

        public static TesseractResultado sinFecha(String texto) {
            return new TesseractResultado(true, null, texto, false);
        }

        public static TesseractResultado fallido() {
            return new TesseractResultado(false, null, null, false);
        }
    }
}
