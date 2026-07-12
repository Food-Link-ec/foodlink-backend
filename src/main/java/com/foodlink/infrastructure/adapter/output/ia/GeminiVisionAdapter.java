package com.foodlink.infrastructure.adapter.output.ia;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GeminiVisionAdapter {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";
    private static final String PROMPT = "Analiza esta imagen de un producto alimenticio. Responde en formato JSON con las claves: "
            + "fecha (fecha de caducidad detectada en formato DD/MM/AAAA o null), "
            + "categoria (categoria del producto), "
            + "descripcion (descripcion breve sugerida), "
            + "confianza (alta, media o baja).";

    private final String apiKey;
    private final String modelo;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GeminiVisionAdapter(@Value("${foodlink.ia.gemini-api-key:placeholder}") String apiKey,
                                @Value("${foodlink.ia.gemini-model:gemini-2.5-flash-lite}") String modelo) {
        this.apiKey = apiKey;
        this.modelo = modelo;
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public GeminiResultado analizarImagen(String imagenBase64, String mimeType) {
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("placeholder")) {
            return GeminiResultado.sinConfiguracion();
        }

        try {
            String cuerpo = construirCuerpoRequest(imagenBase64, mimeType);
            String url = String.format(GEMINI_URL, modelo, apiKey);

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(cuerpo, JSON))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    return GeminiResultado.error("Gemini respondio con estado " + response.code());
                }
                return parsearRespuesta(response.body().string());
            }
        } catch (IOException e) {
            return GeminiResultado.error(e.getMessage());
        }
    }

    private String construirCuerpoRequest(String imagenBase64, String mimeType) throws IOException {
        ObjectNode raiz = objectMapper.createObjectNode();
        ArrayNode contents = raiz.putArray("contents");
        ObjectNode content = contents.addObject();
        ArrayNode parts = content.putArray("parts");

        parts.addObject().put("text", PROMPT);

        ObjectNode inlineDataPart = parts.addObject();
        ObjectNode inlineData = inlineDataPart.putObject("inline_data");
        inlineData.put("mime_type", mimeType);
        inlineData.put("data", imagenBase64);

        return objectMapper.writeValueAsString(raiz);
    }

    private GeminiResultado parsearRespuesta(String cuerpoRespuesta) {
        try {
            JsonNode raiz = objectMapper.readTree(cuerpoRespuesta);
            String texto = raiz.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText(null);

            if (texto == null || texto.isBlank()) {
                return GeminiResultado.error("Gemini no devolvio contenido analizable");
            }

            String textoJson = texto.replace("```json", "").replace("```", "").trim();
            JsonNode analisis = objectMapper.readTree(textoJson);

            String fecha = analisis.path("fecha").asText(null);
            String categoria = analisis.path("categoria").asText(null);
            String descripcion = analisis.path("descripcion").asText(null);
            String confianza = analisis.path("confianza").asText(null);

            return GeminiResultado.exitoso(fecha, categoria, descripcion, confianza);
        } catch (IOException e) {
            return GeminiResultado.error("No se pudo interpretar la respuesta de Gemini");
        }
    }

    public record GeminiResultado(boolean exitoso, String fecha, String categoria, String descripcion, String confianza, String error) {

        public static GeminiResultado exitoso(String fecha, String categoria, String descripcion, String confianza) {
            return new GeminiResultado(true, fecha, categoria, descripcion, confianza, null);
        }

        public static GeminiResultado error(String mensaje) {
            return new GeminiResultado(false, null, null, null, null, mensaje);
        }

        public static GeminiResultado sinConfiguracion() {
            return new GeminiResultado(false, null, null, null, null, "Gemini API key no configurada");
        }
    }
}
