package com.foodlink.infrastructure.adapter.input.rest.controller;

import com.foodlink.application.dto.response.ApiErrorResponse;
import com.foodlink.domain.model.auth.exception.TokenInvalidoException;
import com.foodlink.domain.model.beneficiario.exception.BeneficiarioInvalidoException;
import com.foodlink.domain.model.comercio.exception.ComercioInvalidoException;
import com.foodlink.domain.model.comercio.exception.RucInvalidoException;
import com.foodlink.domain.model.comprador.exception.CedulaInvalidaException;
import com.foodlink.domain.model.comprador.exception.CompradorInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ComercioInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> manejarComercioInvalido(ComercioInvalidoException ex) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(RucInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> manejarRucInvalido(RucInvalidoException ex) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(BeneficiarioInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> manejarBeneficiarioInvalido(BeneficiarioInvalidoException ex) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(CompradorInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> manejarCompradorInvalido(CompradorInvalidoException ex) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(CedulaInvalidaException.class)
    public ResponseEntity<ApiErrorResponse> manejarCedulaInvalida(CedulaInvalidaException ex) {
        return construirRespuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> manejarTokenInvalido(TokenInvalidoException ex) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> manejarIllegalArgument(IllegalArgumentException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> manejarArgumentoInvalido(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return construirRespuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<ApiErrorResponse> construirRespuesta(HttpStatus status, String mensaje) {
        ApiErrorResponse cuerpo = new ApiErrorResponse(status.value(), status.getReasonPhrase(), mensaje, LocalDateTime.now());
        return ResponseEntity.status(status).body(cuerpo);
    }
}
