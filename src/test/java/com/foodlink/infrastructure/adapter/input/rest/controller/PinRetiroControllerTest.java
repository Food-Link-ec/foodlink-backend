package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlink.application.dto.request.ValidarPinRequest;
import com.foodlink.application.dto.response.PinRetiroResponse;
import com.foodlink.domain.port.input.GenerarPinUseCase;
import com.foodlink.domain.port.input.ValidarPinUseCase;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PinRetiroController.class)
@Import(SecurityConfig.class)
class PinRetiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenerarPinUseCase generarPinUseCase;

    @MockBean
    private ValidarPinUseCase validarPinUseCase;

    @MockBean
    private JwtService jwtService;

    private UsuarioAutenticado usuarioAutenticado() {
        return UsuarioAutenticado.of(UUID.randomUUID(), "comercio@test.com", "ROLE_COMERCIO", "COMERCIO");
    }

    @Test
    void generarPinAutenticadoDeberiaRetornar200ConPinYQrData() throws Exception {
        UUID loteId = UUID.randomUUID();
        when(generarPinUseCase.generarPin(loteId)).thenReturn(new PinRetiroResponse(
                loteId, "A3K7F", "FOODLINK:RETIRO:" + loteId + ":A3K7F",
                LocalDateTime.now().plusHours(24), "PIN generado. Válido por 24 horas."));

        mockMvc.perform(post("/api/v1/retiros/pin/{loteId}", loteId)
                        .with(authentication(usuarioAutenticado())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").value("A3K7F"))
                .andExpect(jsonPath("$.qrData").exists());
    }

    @Test
    void generarPinSinAutenticacionDeberiaRetornar403() throws Exception {
        mockMvc.perform(post("/api/v1/retiros/pin/{loteId}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void validarPinAutenticadoDeberiaRetornar200() throws Exception {
        mockMvc.perform(post("/api/v1/retiros/validar")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ValidarPinRequest(UUID.randomUUID(), "A3K7F"))))
                .andExpect(status().isOk());
    }

    @Test
    void validarPinConPinIncorrectoDeberiaRetornar400() throws Exception {
        doThrow(new IllegalArgumentException("PIN inválido"))
                .when(validarPinUseCase).validarPin(any(), any());

        mockMvc.perform(post("/api/v1/retiros/validar")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ValidarPinRequest(UUID.randomUUID(), "WRONG"))))
                .andExpect(status().isBadRequest());
    }
}
