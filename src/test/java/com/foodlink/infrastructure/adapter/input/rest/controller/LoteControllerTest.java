package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlink.application.dto.request.PublicarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.port.input.BuscarLotesUseCase;
import com.foodlink.domain.port.input.PublicarLoteUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.JwtService;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import com.foodlink.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoteController.class)
@Import(SecurityConfig.class)
class LoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicarLoteUseCase publicarLoteUseCase;

    @MockBean
    private BuscarLotesUseCase buscarLotesUseCase;

    @MockBean
    private JwtService jwtService;

    private UsuarioAutenticado usuarioComercio(UUID comercioId) {
        return UsuarioAutenticado.of(comercioId, "comercio@test.com", "ROLE_COMERCIO", "COMERCIO");
    }

    private UsuarioAutenticado usuarioComprador(UUID compradorId) {
        return UsuarioAutenticado.of(compradorId, "comprador@test.com", "ROLE_COMPRADOR", "COMPRADOR");
    }

    private String requestVentaJson() throws Exception {
        PublicarLoteRequest request = new PublicarLoteRequest("VENTA", 10.0, new BigDecimal("30"),
                new BigDecimal("100"), LocalDateTime.now().plusDays(3), "Frutas y verduras frescas",
                List.of("https://foto.com/1.jpg"));
        return objectMapper.writeValueAsString(request);
    }

    private LoteResponse loteResponseVenta(UUID id, UUID comercioId) {
        return new LoteResponse(id, comercioId, "VENTA", "DISPONIBLE", 10.0, new BigDecimal("30"),
                new BigDecimal("100"), LocalDateTime.now().plusDays(3), LocalDateTime.now(),
                "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"));
    }

    private LoteResponse loteResponseDonacion(UUID id, UUID comercioId) {
        return new LoteResponse(id, comercioId, "DONACION", "DISPONIBLE", 10.0, null, null,
                LocalDateTime.now().plusDays(3), LocalDateTime.now(), "Pan del día",
                List.of("https://foto.com/1.jpg"));
    }

    @Test
    void publicarConRolComercioYBodyValidoDeberiaRetornar201() throws Exception {
        UUID comercioId = UUID.randomUUID();
        when(publicarLoteUseCase.publicar(any(), eq(comercioId))).thenReturn(loteResponseVenta(UUID.randomUUID(), comercioId));

        mockMvc.perform(post("/api/v1/lotes")
                        .with(authentication(usuarioComercio(comercioId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestVentaJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void publicarSinAutenticacionDeberiaRetornar403() throws Exception {
        mockMvc.perform(post("/api/v1/lotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestVentaJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void publicarConRolCompradorDeberiaRetornar403() throws Exception {
        mockMvc.perform(post("/api/v1/lotes")
                        .with(authentication(usuarioComprador(UUID.randomUUID())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestVentaJson()))
                .andExpect(status().isForbidden());
    }

    @Test
    void buscarSinAutenticacionDeberiaRetornar200() throws Exception {
        when(buscarLotesUseCase.buscar(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/lotes"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarConModalidadDonacionDeberiaRetornar200ConListaFiltrada() throws Exception {
        when(buscarLotesUseCase.buscar(any())).thenReturn(List.of(loteResponseDonacion(UUID.randomUUID(), UUID.randomUUID())));

        mockMvc.perform(get("/api/v1/lotes").param("modalidad", "DONACION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].modalidad").value("DONACION"));
    }

    @Test
    void buscarPorIdConIdExistenteDeberiaRetornar200() throws Exception {
        UUID id = UUID.randomUUID();
        when(buscarLotesUseCase.buscarPorId(id)).thenReturn(loteResponseVenta(id, UUID.randomUUID()));

        mockMvc.perform(get("/api/v1/lotes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void buscarPorIdConIdInexistenteDeberiaRetornar400() throws Exception {
        UUID id = UUID.randomUUID();
        when(buscarLotesUseCase.buscarPorId(id)).thenThrow(new IllegalArgumentException("Lote no encontrado: " + id));

        mockMvc.perform(get("/api/v1/lotes/{id}", id))
                .andExpect(status().isBadRequest());
    }
}