package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodlink.application.dto.request.CancelarReservaRequest;
import com.foodlink.application.dto.request.ConfirmarDonacionRequest;
import com.foodlink.application.dto.request.ConfirmarVentaRequest;
import com.foodlink.application.dto.request.ReservarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.port.input.ConfirmarTransaccionUseCase;
import com.foodlink.domain.port.input.ReservarLoteUseCase;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedistribucionController.class)
@Import(SecurityConfig.class)
class RedistribucionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservarLoteUseCase reservarLoteUseCase;

    @MockBean
    private ConfirmarTransaccionUseCase confirmarTransaccionUseCase;

    @MockBean
    private JwtService jwtService;

    private UsuarioAutenticado usuarioAutenticado() {
        return UsuarioAutenticado.of(UUID.randomUUID(), "comprador@test.com", "ROLE_COMPRADOR", "COMPRADOR");
    }

    private LoteResponse loteResponse() {
        return new LoteResponse(UUID.randomUUID(), UUID.randomUUID(), "VENTA", "RESERVADO", 10.0,
                new BigDecimal("30"), new BigDecimal("100"), LocalDateTime.now().plusDays(3),
                LocalDateTime.now(), "Frutas y verduras frescas", List.of("https://foto.com/1.jpg"), null, null);
    }

    @Test
    void reservarAutenticadoDeberiaRetornar200() throws Exception {
        when(reservarLoteUseCase.reservar(any(), any())).thenReturn(loteResponse());

        mockMvc.perform(post("/api/v1/redistribucion/reservar")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReservarLoteRequest(UUID.randomUUID()))))
                .andExpect(status().isOk());
    }

    @Test
    void reservarSinAutenticacionDeberiaRetornar403() throws Exception {
        mockMvc.perform(post("/api/v1/redistribucion/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReservarLoteRequest(UUID.randomUUID()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelarAutenticadoDeberiaRetornar200() throws Exception {
        when(reservarLoteUseCase.cancelarReserva(any())).thenReturn(loteResponse());

        mockMvc.perform(post("/api/v1/redistribucion/cancelar")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CancelarReservaRequest(UUID.randomUUID(), "motivo"))))
                .andExpect(status().isOk());
    }

    @Test
    void confirmarVentaAutenticadoDeberiaRetornar200() throws Exception {
        when(confirmarTransaccionUseCase.confirmarVenta(any())).thenReturn(loteResponse());

        mockMvc.perform(post("/api/v1/redistribucion/confirmar-venta")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ConfirmarVentaRequest(UUID.randomUUID(), UUID.randomUUID()))))
                .andExpect(status().isOk());
    }

    @Test
    void confirmarDonacionAutenticadoDeberiaRetornar200() throws Exception {
        when(confirmarTransaccionUseCase.confirmarDonacion(any())).thenReturn(loteResponse());

        mockMvc.perform(post("/api/v1/redistribucion/confirmar-donacion")
                        .with(authentication(usuarioAutenticado()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ConfirmarDonacionRequest(UUID.randomUUID(), UUID.randomUUID()))))
                .andExpect(status().isOk());
    }
}
