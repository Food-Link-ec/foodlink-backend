package com.foodlink.application.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> contenido,
        int paginaActual,
        int totalPaginas,
        long totalElementos,
        int tamanioPagina,
        boolean esUltimaPagina,
        boolean esPrimeraPagina
) {

    public static <T> PageResponse<T> de(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast(),
                page.isFirst()
        );
    }
}
