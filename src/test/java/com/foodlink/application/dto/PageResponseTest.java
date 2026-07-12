package com.foodlink.application.dto;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.response.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageResponseTest {

    @Test
    void deConPageVaciaDeberiaRetornarTotalElementosCero() {
        Page<String> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        PageResponse<String> response = PageResponse.de(page);

        assertEquals(0, response.totalElementos());
    }

    @Test
    void deConPageDeUnElementoDeberiaTenerContenidoConUnElemento() {
        Page<String> page = new PageImpl<>(List.of("lote-1"), PageRequest.of(0, 10), 1);

        PageResponse<String> response = PageResponse.de(page);

        assertEquals(1, response.contenido().size());
    }

    @Test
    void buscarLotesRequestConSizeCeroDeberiaNormalizarADiez() {
        BuscarLotesRequest request = new BuscarLotesRequest(
                null, null, null, null, null, null, null, null, 0, 0);

        assertEquals(10, request.size());
    }

    @Test
    void buscarLotesRequestConSizeMayorACincuentaDeberiaNormalizarADiez() {
        BuscarLotesRequest request = new BuscarLotesRequest(
                null, null, null, null, null, null, null, null, 0, 100);

        assertEquals(10, request.size());
    }

    @Test
    void buscarLotesRequestConPageMenosUnoDeberiaNormalizarACero() {
        BuscarLotesRequest request = new BuscarLotesRequest(
                null, null, null, null, null, null, null, null, -1, 10);

        assertEquals(0, request.page());
    }
}
