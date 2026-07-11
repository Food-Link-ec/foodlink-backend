package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.request.BuscarLotesRequest;
import com.foodlink.application.dto.request.PublicarLoteRequest;
import com.foodlink.application.dto.response.LoteResponse;
import com.foodlink.domain.port.input.BuscarLotesUseCase;
import com.foodlink.domain.port.input.PublicarLoteUseCase;
import com.foodlink.infrastructure.adapter.input.rest.security.CurrentUser;
import com.foodlink.infrastructure.adapter.input.rest.security.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController {

    private final PublicarLoteUseCase publicarLoteUseCase;
    private final BuscarLotesUseCase buscarLotesUseCase;

    public LoteController(
            PublicarLoteUseCase publicarLoteUseCase,
            BuscarLotesUseCase buscarLotesUseCase) {
        this.publicarLoteUseCase = publicarLoteUseCase;
        this.buscarLotesUseCase = buscarLotesUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoteResponse> publicar(
            @Valid @RequestBody PublicarLoteRequest request,
            @CurrentUser UsuarioAutenticado usuario) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publicarLoteUseCase.publicar(request, usuario.getUsuarioId()));
    }

    @GetMapping
    public ResponseEntity<List<LoteResponse>> buscar(
            @RequestParam(required = false) String modalidad,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) UUID comercioId) {
        BuscarLotesRequest request = new BuscarLotesRequest(modalidad, estado, comercioId);
        return ResponseEntity.ok(buscarLotesUseCase.buscar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarLotesUseCase.buscarPorId(id));
    }
}